package wtf.nucker.kitpvpplus.utils.github;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.io.StringReader;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 09/08/2021
 */
public class GithubClient {

    private final String url;

    public GithubClient() {
        this.url = "https://api.github.com/";
    }

    /**
     * @param name For KitPvPPlus by Nuckerr it would be "Nuckerr/KitPvPPlus"
     * @return Instance of repo
     */
    public GithubRepo getRepository(String name) {
        String url = this.url + "repos/" + name;
        JsonElement element = this.get(url);

        return new GithubRepo() {
            @Override
            public GithubRelease getLatestRelease() {
                JsonElement releaseEl = GithubClient.this.get(url + "/" + "releases");
                return () -> releaseEl.getAsJsonArray().get(0).getAsJsonObject().get("tag_name").getAsString();
            }

            @Override
            public String getName() {
                return element.getAsJsonObject().get("name").getAsString();
            }

            @Override
            public String getOwnerName() {
                return element.getAsJsonObject().get("owner").getAsJsonObject().get("login").getAsString();
            }
        };
    }

    private JsonElement get(String stringUrl) {
        HttpResponse req = HttpRequest.get(stringUrl).accept("application/vnd.github.v3+json").send();
        JsonReader reader = new JsonReader(new StringReader(req.bodyText()));
        reader.setLenient(true);
        return new JsonParser().parse(reader);

        /*
        try {

            URLConnection url = new URL(stringUrl).openConnection();
            HttpURLConnection con = (HttpURLConnection) url.
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);


            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                Logger.debug(response);
                return (new JsonParser()).parse(response.toString());
            }
        }catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
             */

    }
}
