package org.grantharper.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.tess4j.Tesseract;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.recipe.ocr.OCRExecutor;
import org.grantharper.recipe.ocr.OCRExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class AppConfig
{

  public static final String PNG_OUTPUT_DIR = "data/output/png";
  public static final String JSON_OUTPUT_DIR = "data/output/json";
  public static final String JPG_INPUT_DIR = "data/input";
  public static final String TEXT_OUTPUT_DIR = "data/output/text";
  public static final String HTML_OUTPUT_DIR = "data/output/html";

  @Bean
  public Rectangle getRecipeViewport()
  {
    return new Rectangle(150, 400, 2250, 2770);
  }

  @Bean
  public OCRExecutor ocrExecutor()
  {
    return new OCRExecutorImpl(new Tesseract());
  }

  @Bean
  public RestHighLevelClient restHighLevelClient()
  {
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

  @Bean
  public ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

}
