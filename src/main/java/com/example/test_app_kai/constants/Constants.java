package com.example.test_app_kai.constants;

public class Constants {

    public static final String BASE_URL = "https://www.traveloka.com";
    public static final String CONTENT_TYPE = "content-type";
    public static final String APPLICATION_JSON = "application/json";

    public static final String USER_AGENT = "user-agent";
    public static final String ROUTE_PREFIX_HEADER = "x-route-prefix";
    public static final String ROUTE_PREFIX_HEADER_VALUE = "en-id";
    public static final String ORIGIN_HEADER = "origin";
    public static final String ORIGIN_HEADER_VALUE = "https://www.traveloka.com";
    public static final String X_DOMAIN_HEADER = "x-domain";
    public static final String X_DOMAIN_HEADER_VALUE = "train";
    public static final String COOKIE_HEADER = "Cookie";
    public static final String COOKIE_HEADER_VALUE = "tv-repeat-visit=true; countryCode=RS; _fbp=fb.1.1751550980117.739880509500464517; _gcl_au=1.1.819545298.1751550980; _gid=GA1.2.1122374577.1751550980; _tt_enable_cookie=1; _ttp=01JZ895WV66254959R4BDD2TX5_.tt.1; tv_user={\\\"authorizationLevel\\\":100,\\\"id\\\":null}; _cs_ex=1747128313; _cs_c=1; ABTastySession=mrasn=; adonis-session=b7e5bfec798a40b02d15ab4b14e45ef0UKtOmrgFiZYnYub5a8rml0%2B7pf%2FBMj%2BNFPuw6xE2bKahKE%2Bn7NwTi8L%2B74TP66OxFNrEmniS1EG9ZL1N%2Fd3r6JahEyYP0FKb3udi%2FL6gLF69q9rEp8%2Fy97g4kgU1biGl; XSRF-TOKEN=964852e83b49dad884b1b0b526e1dc0f2Mwb%2BojhZWWDdKMYaB3lIkqY5f2mft0IhBiREm1c8PM1HhYlxR%2BYKdpUjv1mAXjuVQQUG65jcPrbF1DUa8yppV0yQSbc2PO5kD1Im2hBReplvQQE4ysTQ4JqUycbRqqw; adonis-session-values=9145738179ef095ec91853984fee297biq310cyUxG%2FyFUPK04ffI7jRPUKA7tklTk71kBxqb4MntD3aBgWtGPIsvWntXp21By4XNUvKvVjbu0vNQ1pZC1LBrGR3OhG74VlcP7a%2FGsJXReykveDSGSlw9hrX0Bvg1E1ghtNWIZ%2BAxhBjkpvksAUNqHOp4F1onGTqfGgUKnY%3D; ttcsid_CAG67D3C77U28261LD10=1751551417919::9fAEt7Vbz3AmAv3bYlzb.1.1751551458998; amp_f4354c=BLjcrG1NfaN2sVpurA97wN...1iv895rut.1iv8a3kdd.0.1.1; _ga=GA1.2.56820087.1751550980; cto_bundle=_j5Y7F9kQ0FvR2FZYkVZV0VNUyUyRmU4cXFhNUlBa0VwdXVLTm1KeWlkS3BtYlFpaDZIbHJrcnRRT1F1SW8xWmFHNTB3S1loTFZrS1Boa3FzZTExREk2VkRIN1VoY1RQUkRkV3FxUnU3cDNhdW5tUjY4SW54M2U4ZmY1b3ViYzJkTlI1TWElMkJaUVVSZjdVWmZ3NG5NTzdKMndGN2xUQmQxMWVvUjRZd3BsQ0NFUXloc09FJTNE; ttcsid=1751550980974::ZjWm9seknyiSgQtKSHeD.1.1751551955840; g_state={\\\"i_p\\\":1751559652529,\\\"i_l\\\":1}; amp_1a5adb=nPabkBvRd6SWt9jCJNU2bS...1iv895rus.1iv8aiptq.v.1.10; aws-waf-token=3ea6e4c7-e6f4-42ec-a239-e82aaac3251b:DgoAuOJjxMlDAAAA:7Gq9K522z9b7JGfTqgSHWK7bU1uImc3F9X7Y56whmQU/ldHjhshHNhuEEf0elxz5LSprdRENuqUZ/4bs3PRJWoHWh6KltwWBO0hHvtjzgs3lzZfv0XNU3wzYXU4yodOoDlBSIxvbboUbtlKlnk+W3t1ZIhmf1z0skZqrPzf+OYjcDi8HlkLWeVxPJeH6jORX4QrJCS4K1Q==; tvl=M21/oknbi4ZVNI8t/rSheP6TFuD8sYLdkKQIFftvi73HmyV/5DXXb6ofQ9uM+ONxUjweiVU7bV75VT0IzvdUL2+FUdpaHmgrFB6bVnXGkHN2anIWmt9xopo7h77kDqSdromMY3e86tpwz94Jqy8KhWyQtXcV1aUWW7INePxf9g9/jO0yjinutdDp4xNokQpIWTnBfdOxD+J17dkWabaV+3sLXVJOFmdaG0MqB4GdIdpkTNok2Wle0BNvpjZNc2g6iGwGdOxNIoTM6n71klQN5jUER+qpI1ZRf7+fbXbubhyRmu2y6BsCc1b+vDo+B8H3jrqW0q6/VPsqOBd7SFGi0IRv2Ggn7tyQQ9xGJsGGsfMDNxd0PjSWzOjfEwcNpLLmm5c6ZtgAoFPobFHpAf4oGsOO349WXOgXEDrQzy3plzf54/htfrzx2v6DBA8u4fUb9cPlSvA=~djAy; tvs=eHbCGVDWNSBHKE/Lp8WmM76CC5IIIWKU/OGcMK3aHi3T0ZSSjOuPQsXhsnAQqbSTFIvGlT7bjZDpAErPjYuZErncG/a9CuJb9Vs/wUpRGuyde5XhazKIABd9uPE7C4WOMVsBSPx/JXRY2U1l8k/NZ/zFcLtkPpvw/zk7a1FHTMav8LBe2FJetn4mLeMFU2Us1jC/l/Kyq/a5rdhMUZicJi+Y8s6Te3Lf0LDDQqSXukVGGsVsGXCZbDPjEDgsZXmDCpRXVYpJu+iJ5aqZt6XlmB6k6VdB4/AbSTnSIyYMXDVTuEc6m/72+CwaHQVu8eDWfFpfZkY73Fb8F9SidbzZ2Q4OTjwXBkwlqOBoPLsASstoLON+zc+TMDRjx+m1Mj/NPY9iR6CTOhkm/HT+H2Y8Yu/EFQhZvEkG3Gk3SFjpE2UT0OPczdV/LcPhU8yWVVApbJWWmTsq0A==~djAy; ttcsid_CUM82PBC77U4QKJNCRL0=1751550980973::JGHa8A-pryvaaaQ9SoV8.1.1751552696458; _ga_RSRSMMBH0X=GS2.1.s1751550980^$o1^$g1^$t1751552696^$j60^$l0^$h374008992; _dd_s=rum=0&expire=1751553596664&logs=1&id=62954c4d-3848-471b-a4be-a127f74d867c&created=1751550979679";

    public static final String API_URL = "/api/v2/train/search/inventoryv2";

    public static final String PROVIDER_TYPE = "KAI";
    public static final String CURRENCY_CODE = "IDR";
    public static final String CLIENT_INTERFACE = "desktop";
    public static final String BLOCKED_STATUS = "FULLY_BOOKED";
    public static final String FARE_CODE_TICKET = "saver";
    public static final String TRAIN_CLASS_WITHOUT_CHANGES = "Intercity";
    public static final String JOINING_SEPARATOR = "|";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String SLASH = "/";
    public static final String DOUBLE_UNDERSCORE = "__";
    public static final String CSV_EXTENSION = ".csv";
    public static final int SUCCESS_CODE = 0;
    public static final int SUCCESS_EMPTY_LIST_CODE = 200;
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String SUCCESS_EMPTY_LIST_MESSAGE = "Ticket list is empty";
    public static final String SPLIT_DELIMITER = ", ";
    public static final String HARVESTER_NAME = "KAI";
    public final static String SUCCESS_HARVESTER_MESSAGE = "Success";

    public static final String TRAIN_BRAND_REGEX_PATTERN = "\\s*\\([^)]*\\)";
    public static final String TRAIN_BRAND_REPLACE_PATTERN = "";
    public final static String DASH = "-";
    public final static String SPACE = " ";
    public final static String UNDERSCORE = "_";

    public final static String START_HARVESTER_TYPE = "Start harvesterType: {}";
    public final static String FINISH_HARVESTER_TYPE = "Harvester finish harvester type: {}";
    public final static String UNKNOWN_EXCEPTION = "Unknown exception in {} harvester thread Exc: {} {}";

    public final static int ERROR_HARVESTER_CODE = 231;
    public final static int SUCCESS_HARVESTER_CODE = 0;
    public final static int NULL_TICKET_HARVESTER_CODE = 200;

    public final static int SECOND_1 = 1000;
    public final static int SECOND_55 = 55000;

    public final static String ALL_FINISHED = "All harvestrs threads finished";
    public final static String ONE_FAILED = "One of harvester threads failed";
    public final static String EMPTY_HARVESTER_MESSAGE = "Empty";
    public final static String THREAD_FAILED = "One thread failed";
}
