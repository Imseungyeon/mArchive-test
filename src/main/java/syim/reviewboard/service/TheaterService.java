package syim.reviewboard.service;

import jakarta.persistence.Column;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import syim.reviewboard.dto.TheaterDto;
import syim.reviewboard.model.Theater;
import syim.reviewboard.repository.TheaterRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;

    @Value("${api.theater.kofis.key}")
    private String KOFIS_KEY;

    public List<Theater> getTheaterFromApi(String keyword) {
        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Encoding failed", e);
        }
        String theaterData = getTheaterString(encodedKeyword);

        // 받아온 연극/뮤지컬 정보 json 파싱
        List<TheaterDto> parsedTheaters = parseTheaterXml(theaterData);

        //파싱된 연극/뮤지컬 정보 도메인 Theater에 넣기
        List<Theater> theaters = new ArrayList<>();
        for (int i = 0; i < parsedTheaters.size(); i++){
            Theater theater = new Theater();

            theater.setTitle(parsedTheaters.get(i).getTitle());
            theater.setGenre(parsedTheaters.get(i).getGenre());
            theater.setStartDate(parsedTheaters.get(i).getStartDate());
            theater.setEndDate(parsedTheaters.get(i).getEndDate());
            theater.setImageURL(parsedTheaters.get(i).getImageURL());
            theater.setPlace(parsedTheaters.get(i).getPlace());
            theater.setMt20id(parsedTheaters.get(i).getMt20id());

            theaters.add(theater);
        }
        return theaters;
    }

    //최근 5년 내 작품 검색
    private String getTheaterString(String encodedKeyword){
        String apiURL = "https://www.kopis.or.kr/openApi/restful/pblprfr?service=" + KOFIS_KEY +
                "&stdate=" + LocalDate.now().minusYears(5).format(DateTimeFormatter.ofPattern("YYYYMMDD")) +
                "&eddate=" + LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMMDD")) +
                "&cpage=1&rows=10&shprfnm=" + encodedKeyword;

        try{
            URL url = new URL(apiURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if(responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    // 연극/뮤지컬 정보 검색 api에서 받아온 xml String 파싱, 그 안의 필요한 값 반환
    private List<TheaterDto> parseTheaterXml(String xmlString) {

        List<TheaterDto> theaterDtoList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            Document doc = builder.parse(is);

            NodeList dbList = doc.getElementsByTagName("db");
            for (int i = 0; i < dbList.getLength(); i++) {
                Element dbElement = (Element) dbList.item(i);
                TheaterDto theater = new TheaterDto();

                theater.setMt20id(getTagValue("mt20id", dbElement));
                theater.setTitle(getTagValue("prfnm", dbElement));
                theater.setGenre(getTagValue("genrenm", dbElement));
                theater.setStartDate(getTagValue("prfpdfrom", dbElement));
                theater.setEndDate(getTagValue("prfpdto", dbElement));
                theater.setImageURL(getTagValue("poster", dbElement));
                theater.setPlace(getTagValue("fcltynm", dbElement));

                theaterDtoList.add(theater);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return theaterDtoList;

    }
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }


}
