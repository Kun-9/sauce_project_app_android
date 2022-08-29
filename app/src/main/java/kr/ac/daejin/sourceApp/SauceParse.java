package kr.ac.daejin.sourceApp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class SauceParse {

    // json 저장 메소드
    public void saveSauceList(String str) {
        // 현재 절대경로 값 path에 할당
//        String path = getFilesDir().getAbsolutePath();
        String path = "/data/data/kr.ac.daejin.SourceProjectApp/files";

        try {

            File file = new File(path + "/currentSauceList.json");
            FileOutputStream fos = new FileOutputStream(file);

            // 입력받은 str값을 txt파일에 입력
            fos.write(str.getBytes());
            fos.close();

        } catch (IOException e) {
        }
    }


    public String getCurrentSauceInfo() {
        // 현재 절대경로 값 path에 할당

        StringBuilder sb = new StringBuilder();

        String path = "/data/data/kr.ac.daejin.SourceProjectApp/files";

        // 절대경로에 chatLog.txt 라는 파일로 저장
        File file = new File(path + "/currentSauceList.json");

        try {
            if (file.length() == 0) {
                return null;
            }

            FileInputStream fos = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fos);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fos.close();

            return sb.toString();

        } catch (IOException e) {
            return null;
        }
    }

    public String parseSauceJson(String json) {
        StringBuilder sb = new StringBuilder();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        JsonArray data = jsonElement.getAsJsonArray();

        for (JsonElement sauce : data) {
            JsonObject JsonObject = sauce.getAsJsonObject();
            String id = JsonObject.get("id").getAsString();
            String name = JsonObject.get("name").getAsString();
            String isLiquid = JsonObject.get("isLiquid").getAsString();
            sb.append("id = " + id + " name = " + name + " isLiquid = " + isLiquid).append("\n");
            System.out.println("id = " + id + " name = " + name + " isLiquid = " + isLiquid);
        }
        return sb.toString();
    }
}
