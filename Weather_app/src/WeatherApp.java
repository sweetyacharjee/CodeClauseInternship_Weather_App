import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class WeatherApp{

    private static JFrame frame;
    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static JButton fetchButton;
    @SuppressWarnings("unused")
    private static String apiKey= "01ef974cc58f465859028cc1c3461dd8"; 

    
    private static String fetchWeatherData(String city){
      try{
          //@SuppressWarnings("deprecation")
         
          @SuppressWarnings("deprecation")
          URL url =new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+ apiKey);
          HttpURLConnection connection =(HttpURLConnection) url.openConnection();
          connection .setRequestMethod("GET");

          BufferedReader reader =new BufferedReader(new InputStreamReader(connection .getInputStream()));
          String response="";
          String line;
          while((line = reader.readLine() )!= null){
                  response += line;
          }
          reader.close();

          JSONObject jsonObject  = (JSONObject) JSONValue.parse(response.toString());
          JSONObject mainObj =(JSONObject) jsonObject.get("main");

          double temperatureKelvin = ((Number) mainObj.get("temp")).doubleValue(); 
          long humidity = ((Number) mainObj.get("humidity")).longValue();
          

          double temperatureCelcius =temperatureKelvin - 273.15;

          JSONArray weatherArray =(JSONArray) jsonObject.get("weather");
          JSONObject weather =(JSONObject) weatherArray.get(0);
          String description =(String ) weather.get("description");


          return  "Description: "+ description + "\ntemperature: "+ temperatureCelcius +"Celsius \nHumidity: "+ humidity+" %";
 


      }catch(Exception e){
        return "Failed to fetch weather data , Please check yourcity and api key ... ";
      }
    }


   public static void main(String[] args){
     frame =new JFrame("Weather Forecast App:-");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(450,350);
     frame.setLayout(new FlowLayout());

     locationField=new JTextField(15);
     fetchButton=new JButton("Fetch Weather");
     weatherDisplay =new JTextArea(12,30);
     weatherDisplay.setEditable(false);

     frame.add(new JLabel("Enter City Name"));
     frame.add(locationField);
     frame.add(fetchButton);
     frame.add(weatherDisplay);

     fetchButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            String city =locationField.getText();
            String weatherInfo=fetchWeatherData(city);
            weatherDisplay.setText(weatherInfo);
        }
     });
     frame.setVisible(true);

   }
}