package org.closure.laser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.closure.laser.config.ApplicationProperties;
import org.closure.laser.domain.*;
import org.closure.laser.repository.*;
import org.closure.laser.service.UserApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class LaserApp implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LaserApp.class);

    private final Environment env;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ShipmentTypeRepository shipmentTypeRepository;

    @Autowired
    private UserRepository userRepository;

    public LaserApp(Environment env) {
        this.env = env;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LaserApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }

    /**
     * Initializes laser.
     * <p>
     * Spring profiles can be configured with a program argument
     * --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href=
     * "https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    @Override //
    public void run(ApplicationArguments arg0) throws Exception {
        DealStatus dealStatus = dealStatusRepository.findById(1L).get();
        dealStatus.setName("Waiting");
        dealStatus.setSequence(1);
        dealStatusRepository.save(dealStatus);
        dealStatus = dealStatusRepository.findById(2L).get();
        dealStatus.setName("Pending");
        dealStatus.setSequence(2);
        dealStatusRepository.save(dealStatus);
        dealStatus = dealStatusRepository.findById(3L).get();
        dealStatus.setName("Agreement");
        dealStatus.setSequence(3);
        dealStatusRepository.save(dealStatus);
        dealStatus = dealStatusRepository.findById(4L).get();
        dealStatus.setName("Ready to receive");
        dealStatus.setSequence(4);
        dealStatusRepository.save(dealStatus);
        dealStatus = dealStatusRepository.findById(5L).get();
        dealStatus.setName("Done");
        dealStatus.setSequence(5);
        dealStatusRepository.save(dealStatus);

        countriesRepository.save(new Countries("BD", "Dhaka", "880", "Bangladesh"));
        countriesRepository.save(new Countries("BE", "Brussels", "32", "Belgium"));
        countriesRepository.save(new Countries("BF", "Ouagadougou", "226", "Burkina Faso"));
        countriesRepository.save(new Countries("BG", "Sofia", "359", "Bulgaria"));
        countriesRepository.save(new Countries("BA", "Sarajevo", "387", "Bosnia and Herzegovina"));
        countriesRepository.save(new Countries("BB", "Bridgetown", "+1-246", "Barbados"));
        countriesRepository.save(new Countries("WF", "Mata Utu", "681", "Wallis and Futuna"));
        countriesRepository.save(new Countries("BL", "Gustavia", "590", "Saint Barthelemy"));
        countriesRepository.save(new Countries("BM", "Hamilton", "+1-441", "Bermuda"));
        countriesRepository.save(new Countries("BN", "Bandar Seri Begawan", "673", "Brunei"));
        countriesRepository.save(new Countries("BO", "Sucre", "591", "Bolivia"));
        countriesRepository.save(new Countries("BH", "Manama", "973", "Bahrain"));
        countriesRepository.save(new Countries("BI", "Bujumbura", "257", "Burundi"));
        countriesRepository.save(new Countries("BJ", "Porto-Novo", "229", "Benin"));
        countriesRepository.save(new Countries("BT", "Thimphu", "975", "Bhutan"));
        countriesRepository.save(new Countries("JM", "Kingston", "+1-876", "Jamaica"));
        countriesRepository.save(new Countries("BV", "", "", "Bouvet Island"));
        countriesRepository.save(new Countries("BW", "Gaborone", "267", "Botswana"));
        countriesRepository.save(new Countries("WS", "Apia", "685", "Samoa"));
        countriesRepository.save(new Countries("BQ", "", "599", "Bonaire, Saint Eustatius and Saba "));
        countriesRepository.save(new Countries("BR", "Brasilia", "55", "Brazil"));
        countriesRepository.save(new Countries("BS", "Nassau", "+1-242", "Bahamas"));
        countriesRepository.save(new Countries("JE", "Saint Helier", "+44-1534", "Jersey"));
        countriesRepository.save(new Countries("BY", "Minsk", "375", "Belarus"));
        countriesRepository.save(new Countries("BZ", "Belmopan", "501", "Belize"));
        countriesRepository.save(new Countries("RU", "Moscow", "7", "Russia"));
        countriesRepository.save(new Countries("RW", "Kigali", "250", "Rwanda"));
        countriesRepository.save(new Countries("RS", "Belgrade", "381", "Serbia"));
        countriesRepository.save(new Countries("TL", "Dili", "670", "East Timor"));
        countriesRepository.save(new Countries("RE", "Saint-Denis", "262", "Reunion"));
        countriesRepository.save(new Countries("TM", "Ashgabat", "993", "Turkmenistan"));
        countriesRepository.save(new Countries("TJ", "Dushanbe", "992", "Tajikistan"));
        countriesRepository.save(new Countries("RO", "Bucharest", "40", "Romania"));
        countriesRepository.save(new Countries("TK", "", "690", "Tokelau"));
        countriesRepository.save(new Countries("GW", "Bissau", "245", "Guinea-Bissau"));
        countriesRepository.save(new Countries("GU", "Hagatna", "+1-671", "Guam"));
        countriesRepository.save(new Countries("GT", "Guatemala City", "502", "Guatemala"));
        countriesRepository.save(new Countries("GS", "Grytviken", "", "South Georgia and the South Sandwich Islands"));
        countriesRepository.save(new Countries("GR", "Athens", "30", "Greece"));
        countriesRepository.save(new Countries("GQ", "Malabo", "240", "Equatorial Guinea"));
        countriesRepository.save(new Countries("GP", "Basse-Terre", "590", "Guadeloupe"));
        countriesRepository.save(new Countries("JP", "Tokyo", "81", "Japan"));
        countriesRepository.save(new Countries("GY", "Georgetown", "592", "Guyana"));
        countriesRepository.save(new Countries("GG", "St Peter Port", "+44-1481", "Guernsey"));
        countriesRepository.save(new Countries("GF", "Cayenne", "594", "French Guiana"));
        countriesRepository.save(new Countries("GE", "Tbilisi", "995", "Georgia"));
        countriesRepository.save(new Countries("GD", "St. George's", "+1-473", "Grenada"));
        countriesRepository.save(new Countries("GB", "London", "44", "United Kingdom"));
        countriesRepository.save(new Countries("GA", "Libreville", "241", "Gabon"));
        countriesRepository.save(new Countries("SV", "San Salvador", "503", "El Salvador"));
        countriesRepository.save(new Countries("GN", "Conakry", "224", "Guinea"));
        countriesRepository.save(new Countries("GM", "Banjul", "220", "Gambia"));
        countriesRepository.save(new Countries("GL", "Nuuk", "299", "Greenland"));
        countriesRepository.save(new Countries("GI", "Gibraltar", "350", "Gibraltar"));
        countriesRepository.save(new Countries("GH", "Accra", "233", "Ghana"));
        countriesRepository.save(new Countries("OM", "Muscat", "968", "Oman"));
        countriesRepository.save(new Countries("TN", "Tunis", "216", "Tunisia"));
        countriesRepository.save(new Countries("JO", "Amman", "962", "Jordan"));
        countriesRepository.save(new Countries("HR", "Zagreb", "385", "Croatia"));
        countriesRepository.save(new Countries("HT", "Port-au-Prince", "509", "Haiti"));
        countriesRepository.save(new Countries("HU", "Budapest", "36", "Hungary"));
        countriesRepository.save(new Countries("HK", "Hong Kong", "852", "Hong Kong"));
        countriesRepository.save(new Countries("HN", "Tegucigalpa", "504", "Honduras"));
        countriesRepository.save(new Countries("HM", "", " ", "Heard Island and McDonald Islands"));
        countriesRepository.save(new Countries("VE", "Caracas", "58", "Venezuela"));
        countriesRepository.save(new Countries("PR", "San Juan", "+1-787", "Puerto Rico"));
        countriesRepository.save(new Countries("PR", "San Juan", "1-939", "Puerto Rico"));
        countriesRepository.save(new Countries("PS", "East Jerusalem", "970", "Palestinian Territory"));
        countriesRepository.save(new Countries("PW", "Melekeok", "680", "Palau"));
        countriesRepository.save(new Countries("PT", "Lisbon", "351", "Portugal"));
        countriesRepository.save(new Countries("SJ", "Longyearbyen", "47", "Svalbard and Jan Mayen"));
        countriesRepository.save(new Countries("PY", "Asuncion", "595", "Paraguay"));
        countriesRepository.save(new Countries("IQ", "Baghdad", "964", "Iraq"));
        countriesRepository.save(new Countries("PA", "Panama City", "507", "Panama"));
        countriesRepository.save(new Countries("PF", "Papeete", "689", "French Polynesia"));
        countriesRepository.save(new Countries("PG", "Port Moresby", "675", "Papua New Guinea"));
        countriesRepository.save(new Countries("PE", "Lima", "51", "Peru"));
        countriesRepository.save(new Countries("PK", "Islamabad", "92", "Pakistan"));
        countriesRepository.save(new Countries("PH", "Manila", "63", "Philippines"));
        countriesRepository.save(new Countries("PN", "Adamstown", "870", "Pitcairn"));
        countriesRepository.save(new Countries("PL", "Warsaw", "48", "Poland"));
        countriesRepository.save(new Countries("PM", "Saint-Pierre", "508", "Saint Pierre and Miquelon"));
        countriesRepository.save(new Countries("ZM", "Lusaka", "260", "Zambia"));
        countriesRepository.save(new Countries("EH", "El-Aaiun", "212", "Western Sahara"));
        countriesRepository.save(new Countries("EE", "Tallinn", "372", "Estonia"));
        countriesRepository.save(new Countries("EG", "Cairo", "20", "Egypt"));
        countriesRepository.save(new Countries("ZA", "Pretoria", "27", "South Africa"));
        countriesRepository.save(new Countries("EC", "Quito", "593", "Ecuador"));
        countriesRepository.save(new Countries("IT", "Rome", "39", "Italy"));
        countriesRepository.save(new Countries("VN", "Hanoi", "84", "Vietnam"));
        countriesRepository.save(new Countries("SB", "Honiara", "677", "Solomon Islands"));
        countriesRepository.save(new Countries("ET", "Addis Ababa", "251", "Ethiopia"));
        countriesRepository.save(new Countries("SO", "Mogadishu", "252", "Somalia"));
        countriesRepository.save(new Countries("ZW", "Harare", "263", "Zimbabwe"));
        countriesRepository.save(new Countries("SA", "Riyadh", "966", "Saudi Arabia"));
        countriesRepository.save(new Countries("ES", "Madrid", "34", "Spain"));
        countriesRepository.save(new Countries("ER", "Asmara", "291", "Eritrea"));
        countriesRepository.save(new Countries("ME", "Podgorica", "382", "Montenegro"));
        countriesRepository.save(new Countries("MD", "Chisinau", "373", "Moldova"));
        countriesRepository.save(new Countries("MG", "Antananarivo", "261", "Madagascar"));
        countriesRepository.save(new Countries("MF", "Marigot", "590", "Saint Martin"));
        countriesRepository.save(new Countries("MA", "Rabat", "212", "Morocco"));
        countriesRepository.save(new Countries("MC", "Monaco", "377", "Monaco"));
        countriesRepository.save(new Countries("UZ", "Tashkent", "998", "Uzbekistan"));
        countriesRepository.save(new Countries("MM", "Nay Pyi Taw", "95", "Myanmar"));
        countriesRepository.save(new Countries("ML", "Bamako", "223", "Mali"));
        countriesRepository.save(new Countries("MO", "Macao", "853", "Macao"));
        countriesRepository.save(new Countries("MN", "Ulan Bator", "976", "Mongolia"));
        countriesRepository.save(new Countries("MH", "Majuro", "692", "Marshall Islands"));
        countriesRepository.save(new Countries("MK", "Skopje", "389", "Macedonia"));
        countriesRepository.save(new Countries("MU", "Port Louis", "230", "Mauritius"));
        countriesRepository.save(new Countries("MT", "Valletta", "356", "Malta"));
        countriesRepository.save(new Countries("MW", "Lilongwe", "265", "Malawi"));
        countriesRepository.save(new Countries("MV", "Male", "960", "Maldives"));
        countriesRepository.save(new Countries("MQ", "Fort-de-France", "596", "Martinique"));
        countriesRepository.save(new Countries("MP", "Saipan", "+1-670", "Northern Mariana Islands"));
        countriesRepository.save(new Countries("MS", "Plymouth", "+1-664", "Montserrat"));
        countriesRepository.save(new Countries("MR", "Nouakchott", "222", "Mauritania"));
        countriesRepository.save(new Countries("IM", "Douglas, Isle of Man ", "+44-1624", "Isle of Man"));
        countriesRepository.save(new Countries("UG", "Kampala", "256", "Uganda"));
        countriesRepository.save(new Countries("TZ", "Dodoma", "255", "Tanzania"));
        countriesRepository.save(new Countries("MY", "Kuala Lumpur", "60", "Malaysia"));
        countriesRepository.save(new Countries("MX", "Mexico City", "52", "Mexico"));
        countriesRepository.save(new Countries("FR", "Paris", "33", "France"));
        countriesRepository.save(new Countries("IO", "Diego Garcia", "246", "British Indian Ocean Territory"));
        countriesRepository.save(new Countries("SH", "Jamestown", "290", "Saint Helena"));
        countriesRepository.save(new Countries("FI", "Helsinki", "358", "Finland"));
        countriesRepository.save(new Countries("FJ", "Suva", "679", "Fiji"));
        countriesRepository.save(new Countries("FK", "Stanley", "500", "Falkland Islands"));
        countriesRepository.save(new Countries("FM", "Palikir", "691", "Micronesia"));
        countriesRepository.save(new Countries("FO", "Torshavn", "298", "Faroe Islands"));
        countriesRepository.save(new Countries("NI", "Managua", "505", "Nicaragua"));
        countriesRepository.save(new Countries("NL", "Amsterdam", "31", "Netherlands"));
        countriesRepository.save(new Countries("NO", "Oslo", "47", "Norway"));
        countriesRepository.save(new Countries("NA", "Windhoek", "264", "Namibia"));
        countriesRepository.save(new Countries("VU", "Port Vila", "678", "Vanuatu"));
        countriesRepository.save(new Countries("NC", "Noumea", "687", "New Caledonia"));
        countriesRepository.save(new Countries("NE", "Niamey", "227", "Niger"));
        countriesRepository.save(new Countries("NF", "Kingston", "672", "Norfolk Island"));
        countriesRepository.save(new Countries("NG", "Abuja", "234", "Nigeria"));
        countriesRepository.save(new Countries("NZ", "Wellington", "64", "New Zealand"));
        countriesRepository.save(new Countries("NP", "Kathmandu", "977", "Nepal"));
        countriesRepository.save(new Countries("NR", "Yaren", "674", "Nauru"));
        countriesRepository.save(new Countries("NU", "Alofi", "683", "Niue"));
        countriesRepository.save(new Countries("CK", "Avarua", "682", "Cook Islands"));
        countriesRepository.save(new Countries("XK", "Pristina", "", "Kosovo"));
        countriesRepository.save(new Countries("CI", "Yamoussoukro", "225", "Ivory Coast"));
        countriesRepository.save(new Countries("CH", "Berne", "41", "Switzerland"));
        countriesRepository.save(new Countries("CO", "Bogota", "57", "Colombia"));
        countriesRepository.save(new Countries("CN", "Beijing", "86", "China"));
        countriesRepository.save(new Countries("CM", "Yaounde", "237", "Cameroon"));
        countriesRepository.save(new Countries("CL", "Santiago", "56", "Chile"));
        countriesRepository.save(new Countries("CC", "West Island", "61", "Cocos Islands"));
        countriesRepository.save(new Countries("CA", "Ottawa", "1", "Canada"));
        countriesRepository.save(new Countries("CG", "Brazzaville", "242", "Republic of the Congo"));
        countriesRepository.save(new Countries("CF", "Bangui", "236", "Central African Republic"));
        countriesRepository.save(new Countries("CD", "Kinshasa", "243", "Democratic Republic of the Congo"));
        countriesRepository.save(new Countries("CZ", "Prague", "420", "Czech Republic"));
        countriesRepository.save(new Countries("CY", "Nicosia", "357", "Cyprus"));
        countriesRepository.save(new Countries("CX", "Flying Fish Cove", "61", "Christmas Island"));
        countriesRepository.save(new Countries("CR", "San Jose", "506", "Costa Rica"));
        countriesRepository.save(new Countries("CW", " Willemstad", "599", "Curacao"));
        countriesRepository.save(new Countries("CV", "Praia", "238", "Cape Verde"));
        countriesRepository.save(new Countries("CU", "Havana", "53", "Cuba"));
        countriesRepository.save(new Countries("SZ", "Mbabane", "268", "Swaziland"));
        countriesRepository.save(new Countries("SY", "Damascus", "963", "Syria"));
        countriesRepository.save(new Countries("SX", "Philipsburg", "599", "Sint Maarten"));
        countriesRepository.save(new Countries("KG", "Bishkek", "996", "Kyrgyzstan"));
        countriesRepository.save(new Countries("KE", "Nairobi", "254", "Kenya"));
        countriesRepository.save(new Countries("SS", "Juba", "211", "South Sudan"));
        countriesRepository.save(new Countries("SR", "Paramaribo", "597", "Suriname"));
        countriesRepository.save(new Countries("KI", "Tarawa", "686", "Kiribati"));
        countriesRepository.save(new Countries("KH", "Phnom Penh", "855", "Cambodia"));
        countriesRepository.save(new Countries("KN", "Basseterre", "+1-869", "Saint Kitts and Nevis"));
        countriesRepository.save(new Countries("KM", "Moroni", "269", "Comoros"));
        countriesRepository.save(new Countries("ST", "Sao Tome", "239", "Sao Tome and Principe"));
        countriesRepository.save(new Countries("SK", "Bratislava", "421", "Slovakia"));
        countriesRepository.save(new Countries("KR", "Seoul", "82", "South Korea"));
        countriesRepository.save(new Countries("SI", "Ljubljana", "386", "Slovenia"));
        countriesRepository.save(new Countries("KP", "Pyongyang", "850", "North Korea"));
        countriesRepository.save(new Countries("KW", "Kuwait City", "965", "Kuwait"));
        countriesRepository.save(new Countries("SN", "Dakar", "221", "Senegal"));
        countriesRepository.save(new Countries("SM", "San Marino", "378", "San Marino"));
        countriesRepository.save(new Countries("SL", "Freetown", "232", "Sierra Leone"));
        countriesRepository.save(new Countries("SC", "Victoria", "248", "Seychelles"));
        countriesRepository.save(new Countries("KZ", "Astana", "7", "Kazakhstan"));
        countriesRepository.save(new Countries("KY", "George Town", "+1-345", "Cayman Islands"));
        countriesRepository.save(new Countries("SG", "Singapur", "65", "Singapore"));
        countriesRepository.save(new Countries("SE", "Stockholm", "46", "Sweden"));
        countriesRepository.save(new Countries("SD", "Khartoum", "249", "Sudan"));
        countriesRepository.save(new Countries("DO", "Santo Domingo", "+1-809", "Dominican Republic"));
        countriesRepository.save(new Countries("DO", "Santo Domingo", "1-829", "Dominican Republic"));
        countriesRepository.save(new Countries("DM", "Roseau", "+1-767", "Dominica"));
        countriesRepository.save(new Countries("DJ", "Djibouti", "253", "Djibouti"));
        countriesRepository.save(new Countries("DK", "Copenhagen", "45", "Denmark"));
        countriesRepository.save(new Countries("VG", "Road Town", "+1-284", "British Virgin Islands"));
        countriesRepository.save(new Countries("DE", "Berlin", "49", "Germany"));
        countriesRepository.save(new Countries("YE", "Sanaa", "967", "Yemen"));
        countriesRepository.save(new Countries("DZ", "Algiers", "213", "Algeria"));
        countriesRepository.save(new Countries("US", "Washington", "1", "United States"));
        countriesRepository.save(new Countries("UY", "Montevideo", "598", "Uruguay"));
        countriesRepository.save(new Countries("YT", "Mamoudzou", "262", "Mayotte"));
        countriesRepository.save(new Countries("UM", "", "1", "United States Minor Outlying Islands"));
        countriesRepository.save(new Countries("LB", "Beirut", "961", "Lebanon"));
        countriesRepository.save(new Countries("LC", "Castries", "+1-758", "Saint Lucia"));
        countriesRepository.save(new Countries("LA", "Vientiane", "856", "Laos"));
        countriesRepository.save(new Countries("TV", "Funafuti", "688", "Tuvalu"));
        countriesRepository.save(new Countries("TW", "Taipei", "886", "Taiwan"));
        countriesRepository.save(new Countries("TT", "Port of Spain", "+1-868", "Trinidad and Tobago"));
        countriesRepository.save(new Countries("TR", "Ankara", "90", "Turkey"));
        countriesRepository.save(new Countries("LK", "Colombo", "94", "Sri Lanka"));
        countriesRepository.save(new Countries("LI", "Vaduz", "423", "Liechtenstein"));
        countriesRepository.save(new Countries("LV", "Riga", "371", "Latvia"));
        countriesRepository.save(new Countries("TO", "Nuku'alofa", "676", "Tonga"));
        countriesRepository.save(new Countries("LT", "Vilnius", "370", "Lithuania"));
        countriesRepository.save(new Countries("LU", "Luxembourg", "352", "Luxembourg"));
        countriesRepository.save(new Countries("LR", "Monrovia", "231", "Liberia"));
        countriesRepository.save(new Countries("LS", "Maseru", "266", "Lesotho"));
        countriesRepository.save(new Countries("TH", "Bangkok", "66", "Thailand"));
        countriesRepository.save(new Countries("TF", "Port-aux-Francais", "", "French Southern Territories"));
        countriesRepository.save(new Countries("TG", "Lome", "228", "Togo"));
        countriesRepository.save(new Countries("TD", "N'Djamena", "235", "Chad"));
        countriesRepository.save(new Countries("TC", "Cockburn Town", "+1-649", "Turks and Caicos Islands"));
        countriesRepository.save(new Countries("LY", "Tripolis", "218", "Libya"));
        countriesRepository.save(new Countries("VA", "Vatican City", "379", "Vatican"));
        countriesRepository.save(new Countries("VC", "Kingstown", "+1-784", "Saint Vincent and the Grenadines"));
        countriesRepository.save(new Countries("AE", "Abu Dhabi", "971", "United Arab Emirates"));
        countriesRepository.save(new Countries("AD", "Andorra la Vella", "376", "Andorra"));
        countriesRepository.save(new Countries("AG", "St. John's", "+1-268", "Antigua and Barbuda"));
        countriesRepository.save(new Countries("AF", "Kabul", "93", "Afghanistan"));
        countriesRepository.save(new Countries("AI", "The Valley", "+1-264", "Anguilla"));
        countriesRepository.save(new Countries("VI", "Charlotte Amalie", "+1-340", "U.S. Virgin Islands"));
        countriesRepository.save(new Countries("IS", "Reykjavik", "354", "Iceland"));
        countriesRepository.save(new Countries("IR", "Tehran", "98", "Iran"));
        countriesRepository.save(new Countries("AM", "Yerevan", "374", "Armenia"));
        countriesRepository.save(new Countries("AL", "Tirana", "355", "Albania"));
        countriesRepository.save(new Countries("AO", "Luanda", "244", "Angola"));
        countriesRepository.save(new Countries("AQ", "", "", "Antarctica"));
        countriesRepository.save(new Countries("AS", "Pago Pago", "+1-684", "American Samoa"));
        countriesRepository.save(new Countries("AR", "Buenos Aires", "54", "Argentina"));
        countriesRepository.save(new Countries("AU", "Canberra", "61", "Australia"));
        countriesRepository.save(new Countries("AT", "Vienna", "43", "Austria"));
        countriesRepository.save(new Countries("AW", "Oranjestad", "297", "Aruba"));
        countriesRepository.save(new Countries("IN", "New Delhi", "91", "India"));
        countriesRepository.save(new Countries("AX", "Mariehamn", "+358-18", "Aland Islands"));
        countriesRepository.save(new Countries("AZ", "Baku", "994", "Azerbaijan"));
        countriesRepository.save(new Countries("IE", "Dublin", "353", "Ireland"));
        countriesRepository.save(new Countries("ID", "Jakarta", "62", "Indonesia"));
        countriesRepository.save(new Countries("UA", "Kiev", "380", "Ukraine"));
        countriesRepository.save(new Countries("QA", "Doha", "974", "Qatar"));
        countriesRepository.save(new Countries("MZ", "Maputo", "258", "Mozambique"));
        // /* user Application */

        // User user = userRepository.findById(1L).get();
        // user.setId(null);
        // user.setLogin("laser");
        // user.setFirstName("anas"); ////
        // user.setLastName("altarzi");
        // user.setPassword("1234567890");
        // user.setEmail("email@gmail.com");

        // UserApplication userApplication = userApplicationRepository.findById(1L).get();
        // userApplication.setId(null);
        // userApplication.setUser(user);
        // userApplication.setConnection(connectionRepository.findById(1L).get());

        // userApplicationService.save(userApplication);

        // /* first tripDeal */

        // Deal tripDeal = dealRepository.findById(1L).get();
        // tripDeal.setDeliver(userApplicationRepository.findById(11L).get());
        // tripDeal.setTransaction(transactionRepository.findById(1L).get());
        // tripDeal.setStatus(dealStatusRepository.findById(1L).get());

        // Trip trip = tripRepository.findById(1L).get();
        // trip.setCreatedAt(new Date());
        // trip.setFrom(locationRepository.findById(1L).get());
        // trip.setTo(locationRepository.findById(2L).get());
        // trip = tripRepository.save(trip);

        // tripDeal.setTrip(trip);
        // dealRepository.save(tripDeal);

        // /* second tripDeal */

        // Deal tripDeal1 = dealRepository.findById(2L).get();
        // tripDeal1.setDeliver(userApplicationRepository.findById(11L).get());
        // tripDeal1.setTransaction(transactionRepository.findById(2L).get());
        // tripDeal1.setStatus(dealStatusRepository.findById(2L).get());

        // Trip trip1 = tripRepository.findById(2L).get();
        // trip1.setCreatedAt(new Date());
        // trip1.setFrom(locationRepository.findById(3L).get());
        // trip1.setTo(locationRepository.findById(4L).get());
        // trip1 = tripRepository.save(trip1);

        // tripDeal1.setTrip(trip1);//
        // dealRepository.save(tripDeal1);

        // /* first shipmentDeal */

        // Deal shipmentDeal = dealRepository.findById(3L).get();

        // shipmentDeal.setOwner(userApplicationRepository.findById(11L).get());
        // shipmentDeal.setTransaction(transactionRepository.findById(3L).get());
        // shipmentDeal.setStatus(dealStatusRepository.findById(3L).get());

        // Shipment shipment1 = shipmentRepository.findById(1L).get();
        // shipment1.setCreatedAt(new Date());
        // shipment1.setFrom(locationRepository.findById(5L).get());
        // shipment1.setTo(locationRepository.findById(6L).get());
        // shipment1.setType(shipmentTypeRepository.findById(1L).get());
        // shipment1.setDeal(shipmentDeal);

        // shipment1 = shipmentRepository.save(shipment1);

        // Shipment shipment2 = shipmentRepository.findById(2L).get();
        // shipment2.setCreatedAt(new Date());
        // shipment2.setFrom(locationRepository.findById(7L).get());
        // shipment2.setTo(locationRepository.findById(8L).get());
        // shipment2.setType(shipmentTypeRepository.findById(2L).get());
        // shipment2.setDeal(shipmentDeal);

        // shipment2 = shipmentRepository.save(shipment2);

        // shipmentDeal.getShipments().add(shipment1);
        // shipmentDeal.getShipments().add(shipment2);

        // dealRepository.save(shipmentDeal);

        // /* second shipmentDeal */

        // Deal shipmentDeal1 = dealRepository.findById(4L).get();

        // shipmentDeal1.setOwner(userApplicationRepository.findById(11L).get());
        // shipmentDeal1.setTransaction(transactionRepository.findById(4L).get());
        // shipmentDeal1.setStatus(dealStatusRepository.findById(4L).get());

        // Shipment shipment3 = shipmentRepository.findById(3L).get();
        // shipment3.setCreatedAt(new Date());
        // shipment3.setFrom(locationRepository.findById(9L).get());
        // shipment3.setTo(locationRepository.findById(10L).get());
        // shipment3.setType(shipmentTypeRepository.findById(3L).get());
        // shipment3.setDeal(shipmentDeal1);

        // shipment3 = shipmentRepository.save(shipment3);

        // Shipment shipment4 = shipmentRepository.findById(4L).get();
        // shipment4.setCreatedAt(new Date());
        // shipment4.setFrom(locationRepository.findById(1L).get());
        // shipment4.setTo(locationRepository.findById(2L).get());
        // shipment4.setType(shipmentTypeRepository.findById(4L).get());
        // shipment4.setDeal(shipmentDeal1);

        // shipment4 = shipmentRepository.save(shipment4);

        // shipmentDeal1.getShipments().add(shipment3);
        // shipmentDeal1.getShipments().add(shipment4);

        // dealRepository.save(shipmentDeal1);
    }
}
