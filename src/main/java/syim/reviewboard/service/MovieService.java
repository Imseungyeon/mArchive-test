package syim.reviewboard.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import syim.reviewboard.dto.MovieDto;
import syim.reviewboard.model.Movie;
import syim.reviewboard.repository.MovieRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Value("${api.movie.kofic.key}")
    private String KOFIC_KEY;

    public List<Movie> getMovieFromApi(String keyword) {
        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Encoding failed", e);
        }
        String movieData = getMovieString(encodedKeyword);

        // 받아온 영화 정보 json 파싱
        List<MovieDto> parsedMovies = parseMovie(movieData);

        //파싱된 영화 정보 도메인 Movie에 넣기
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < parsedMovies.size(); i++){
            Movie movie = new Movie();

            movie.setMovieId(parsedMovies.get(i).getMovieId());
            movie.setTitle(parsedMovies.get(i).getTitle());
            movie.setEnglishTitle(parsedMovies.get(i).getEnglishTitle());
            movie.setProductionYear(parsedMovies.get(i).getProductionYear());
            movie.setDirector(parsedMovies.get(i).getDirector());
            movie.setGenre(parsedMovies.get(i).getGenre());
            movie.setNation(parsedMovies.get(i).getNation());

            movies.add(movie);
        }
        return movies;
    }

    private String getMovieString(String encodedKeyword){
        String apiURL = "https://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key="
                + KOFIC_KEY +"&movieNm=" + encodedKeyword;
        try{
            URL url = new URL(apiURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
//            connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
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

    //영화 정보 검색 api에서 받아온 String 파싱, 그 안의 필요한 값 반환
    private List<MovieDto> parseMovie(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject movieListResult = (JSONObject) jsonObject.get("movieListResult");
        JSONArray jsonArray = (JSONArray) movieListResult.get("movieList");
        List<MovieDto> movieDtoList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String title = object.get("movieNm").toString();
            String englishTitle = object.get("movieNmEn") != null ? object.get("movieNmEn").toString() : "Unknown";
            String productionYear = object.get("prdtYear") != null ? object.get("prdtYear").toString() : "Unknown";
            String genre = object.get("repGenreNm") != null ? object.get("repGenreNm").toString() : "Unknown";
            String nation = object.get("repNationNm") != null ? object.get("repNationNm").toString() : "Unknown";
            // 영화 고유 번호 - movieCd
            String movieId = object.get("movieCd").toString();

            JSONArray directorsArray = (JSONArray) object.get("directors");
            String director = "Unknown";
            if (directorsArray != null && !directorsArray.isEmpty()) {
                JSONObject directorObject = (JSONObject) directorsArray.get(0);
                director = directorObject.get("peopleNm").toString();
            }
            movieDtoList.add(MovieDto.builder()
                    .title(title)
                    .englishTitle(englishTitle)
                    .productionYear(productionYear)
                    .director(director)
                    .genre(genre)
                    .nation(nation)
                    .movieId(movieId)
                    .build());
        }

        return movieDtoList;
    }
}
