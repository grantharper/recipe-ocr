package org.grantharper.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.tess4j.Tesseract;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.recipe.ocr.OCRExecutor;
import org.grantharper.recipe.ocr.OCRExecutorImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.awt.*;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig
{

  private static final Logger logger = LogManager.getLogger(AppConfig.class);

  @Value("${rectangle.x: 150}")
  private int rectangleX;

  @Value("${rectangle.y: 400}")
  private int rectangleY;

  @Value("${rectangle.width: 2250}")
  private int rectangleWidth;

  @Value("${rectangle.height: 2770}")
  private int rectangleHeight;

  @Bean
  public Rectangle getRecipeViewport()
  {
    return new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight);
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
