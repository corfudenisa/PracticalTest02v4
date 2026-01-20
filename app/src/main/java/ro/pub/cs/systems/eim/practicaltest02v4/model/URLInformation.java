package ro.pub.cs.systems.eim.practicaltest02v4.model;


import androidx.annotation.NonNull;

public class URLInformation {

    private final String word;
    private final String definition;

    public URLInformation(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    @NonNull
    @Override
    public String toString() {
        return "DictionaryInformation{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
