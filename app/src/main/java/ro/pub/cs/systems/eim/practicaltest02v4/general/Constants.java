package ro.pub.cs.systems.eim.practicaltest02v4.general;

public class Constants {

    // ===================== GENERALE =====================

    // TAG pentru Logcat
    public static final String TAG = "[ColocviuDictionar]";

    // string gol
    public static final String EMPTY_STRING = "";

    // ===================== CERINTA 3 (Dictionary) =====================

    // (3a) URL serviciu dictionar
    // exemplu: https://api.dictionaryapi.dev/api/v2/entries/en/hello
    public static final String DICT_SERVICE =
            "https://api.dictionaryapi.dev/api/v2/entries/en/";

    // ===================== BROADCAST =====================

    // (3c) action pentru broadcast
    public static final String ACTION_DICTIONARY =
            "ro.pub.cs.systems.eim.colocviudictionar.DICTIONARY";

    // (3c) extra cu definitia
    public static final String EXTRA_DICTIONARY_RESULT =
            "dictionary_result";

    // ===================== JSON PARSING =====================

    // prima intrare din array
    public static final int FIRST_INDEX = 0;

    // ===================== TCP / SOCKET =====================

    // marker sfarsit mesaj (client citeste pana la END)
    public static final String END = "END";

    // ===================== DEFAULT (test rapid) =====================

    // emulator -> laptop (daca serverul e pe laptop)
    public static final String DEFAULT_SERVER_ADDRESS = "10.0.2.2";

    // port default
    public static final int DEFAULT_SERVER_PORT = 2026;
}
