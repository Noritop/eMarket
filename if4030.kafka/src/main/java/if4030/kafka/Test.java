package if4030.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.io.FileReader;
import java.util.Locale;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Test {
        // Names of the topics
        public static final String INPUT_TOPIC = "lines-stream";
        public static final String WORDS_STREAM = "words-stream";
        public static final String TAGGED_WORDS_STREAM = "tagged-words-stream";
        public static final String COMMAND_TOPIC = "command-topic";

        // pattern to check the validity of the word
        public static final Pattern pattern = Pattern.compile("^([a-z]|-)*$");

        // list of types of words accepted
        public static final List<String> types = Arrays.asList( "NOM", "VER", "ADV" );

        // dictionnaries to store the lexique and the numbers of words
        public static HashMap<String, String> dictLemme = new HashMap<String, String>();
        public static HashMap<String, String> dictCategory = new HashMap<String, String>();
        public static ArrayList<HashMap<String, Integer>> dictNumber = new ArrayList<HashMap<String, Integer>>();

        // get the properties
        static Properties getStreamsConfig(final String[] args) throws IOException {
                final Properties props = new Properties();
                if ( args != null && args.length > 0 ) {
                        try ( final FileInputStream fis = new FileInputStream( args[0] ) ) {
                                props.load(fis);
                        }
                        if ( args.length > 1 ) {
                                System.out.println("Warning: Some command line arguments were ignored. This demo only accepts an optional configuration file.");
                        }
                }
                props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "tp_kafka");
                props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                props.putIfAbsent(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
                props.putIfAbsent(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
                props.putIfAbsent(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
                props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                return props;
        }


        // initialize dictNumber
        static void initDictNumber() {
                for (int i = 0; i < types.size(); i++) { dictNumber.add( new HashMap<String, Integer>() ); }
        }

        // filter the words
        static List<String> filterWords( String line ) {
                List<String> filtered = new ArrayList<>();
                List<String> lowercase = Arrays.asList( line.toLowerCase(Locale.getDefault()).split(" ") );
                for (String word : lowercase) {
                        Matcher matcher = pattern.matcher( word );
                        if ( matcher.matches() ) { filtered.add( word ); }
                }
                return filtered;
        }


        // creation of the stream to read the input
        static void createWordStream( final StreamsBuilder builder ) {
                final KStream<String, String> source = builder.stream( INPUT_TOPIC );
                final KStream<String, String> words = source.flatMapValues( value -> filterWords( value ) );
                words.to( WORDS_STREAM );
        }


        // creation of the lexique
        static void createLexique() throws IOException {
                FileReader fileReader;
                BufferedReader reader;
                File file = new File( "/config/workspace/if4030.kafka/lexique/Lexique383.tsv" );
                try {
                        fileReader = new FileReader( file );
                        reader = new BufferedReader( fileReader );
                        String line = null;
                        while( ( line = reader.readLine() ) != null ) {
                                List<String> splittedLine = Arrays.asList( line.split("\t") );
                                if ( types.contains( splittedLine.get( 3 ) ) ) {
                                        dictLemme.put( splittedLine.get( 0 ), splittedLine.get( 2 ) );
                                        dictCategory.put( splittedLine.get( 0 ), splittedLine.get( 3 ) );
                                }
                        }
                        reader.close();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }
        }


        // creation of the stream to convert each word via the lexique
        static void createTaggedWordsStream( final StreamsBuilder builder ) {
                final KStream<String, String> source = builder.stream( WORDS_STREAM );
                final KStream<String, String> taggedWords = source
                        .mapValues( value -> dictLemme.get( value ))
                        .map((key, value) -> KeyValue.pair( dictCategory.get( value ), value ));
                taggedWords.to( TAGGED_WORDS_STREAM );
        }


        // sort the words by number
        private static Map<String, Integer> sortByNumber( Map<String, Integer> unsortMap ) {
                Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
                List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>( unsortMap.entrySet() );
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                        public int compare(Map.Entry<String, Integer> Map1,
                                        Map.Entry<String, Integer> Map2) {
                        return -( Map1.getValue() ).compareTo( Map2.getValue() );
                        }
                });
                for ( Map.Entry<String, Integer> entry : list ) {
                        sortedMap.put( entry.getKey(), entry.getValue() );
                }
                return sortedMap;
        }


        // show the words with the maximum occurences by category
        static void countByCategory( final StreamsBuilder builder, Integer limit ) {
                final KStream<String, String> source = builder.stream( TAGGED_WORDS_STREAM );
                final KStream<String, String> sourceCmd = builder.stream( COMMAND_TOPIC );

                source.foreach((key, value) -> {
                        if ( value != null ) {
                                int number = 0;
                                Integer oldNumber = dictNumber.get( types.indexOf( key ) ).get( value );
                                if ( oldNumber != null ) { number += oldNumber.intValue(); }
                                if ( value.length() > 3 ) { dictNumber.get( types.indexOf( key ) ).put( value, number + 1 ); }
                        }
                });

                sourceCmd.foreach((key, value) -> {
                        if ( value.compareTo( "END" ) == 0 ) {
                                for (int i = 0; i < types.size(); i++) {
                                        System.out.println( "\nCategory: " + types.get( i ) );
                                        Map<String, Integer> sortedMap = sortByNumber( dictNumber.get( i ) );
                                        int j = 0;
                                        for ( Map.Entry<String, Integer> input : sortedMap.entrySet() ) {
                                                if (j >= limit ) { break; }
                                                System.out.println( "\t" + input.getKey() + " -> " + input.getValue() );
                                                j++;
                                        }
                                }
                        }
                });
        }

        public static void main(final String[] args) throws IOException {
                final Properties props = getStreamsConfig(args);

                // initialize dictNumber
                initDictNumber();

                // create the lexique
                createLexique();

                // create the streams
                final StreamsBuilder streamBuilder = new StreamsBuilder();
                createWordStream( streamBuilder );
                createTaggedWordsStream( streamBuilder ); 
                countByCategory( streamBuilder, 20 );
                final KafkaStreams streams = new KafkaStreams( streamBuilder.build(), props );
                final CountDownLatch latch = new CountDownLatch( 1 );

                // attach shutdown handler to catch control-c
                Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
                        @Override
                        public void run() {
                        streams.close();
                        latch.countDown();
                        }
                });

                try {
                        streams.start();
                        latch.await();
                } catch (final Throwable e) {
                        System.exit(1);
                }
                System.exit(0);
        }
}
