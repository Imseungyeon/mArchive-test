package syim.reviewboard.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import syim.reviewboard.dto.BookDto;
import syim.reviewboard.model.Book;
import syim.reviewboard.repository.BookRepository;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Value("${api.book.client.id}")
    private String CLIENT_ID;
    @Value("${api.book.client.secret}")
    private String CLIENT_SECRET;

    //BookString Api에서 받아오기
    public List<Book> getBookFromApi(String keyword) {
        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Encoding failed", e);
        }
        String bookData = getBookString(encodedKeyword);

        // 받아온 책 정보 json 파싱
        List<BookDto> parsedBooks = parseBook(bookData);

        //파싱된 책 정보 도메인 Book에 넣기
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < parsedBooks.size(); i++){
            Book book = new Book();
            book.setApiId(parsedBooks.get(i).getApiId());
            book.setTitle(parsedBooks.get(i).getTitle());
            book.setAuthor(parsedBooks.get(i).getAuthor());
            book.setImageURL(parsedBooks.get(i).getImageURL());

            books.add(book);
        }
        return books;
    }
    private String getBookString(String encodedKeyword){
        String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + encodedKeyword;
        try{
            URL url = new URL(apiURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
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

    //naver 책검색 api에서 받아온 String 파싱, 그 안의 필요한 값 반환
    private List<BookDto> parseBook(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        List<BookDto> bookDtoList = new ArrayList<>();

        for (int i = 0; jsonArray.size() > i; i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String title = object.get("title").toString();
            String author = object.get("author")!= null ? (String) object.get("author") : "Unknown";
            String image = object.get("image")!= null ? (String) object.get("image") : "";
            String apiId = object.get("link") != null ? (String) object.get("link") : ""; //책 고유 Id

            bookDtoList.add(BookDto.builder()
                    .title(title)
                    .author(author)
                    .imageURL(image)
                    .apiId(apiId)
                    .build());
        }

        return bookDtoList;
    }

}
