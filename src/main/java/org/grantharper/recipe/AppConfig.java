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

  @Value("${rectangle.x1}")
  private int rectangleX1;

  @Value("${rectangle.x2}")
  private int rectangleX2;

  @Value("${rectangle.y1}")
  private int rectangleY1;

  @Value("${rectangle.y2}")
  private int rectangleY2;

  @Value("${elasticsearch.hostname}")
  private String elasticsearchHostname;

  private Rectangle determineRectangleDimensions()
  {
    int rectangleWidth = rectangleX2 - rectangleX1;
    int rectangleHeight = rectangleY2 - rectangleY1;
    return new Rectangle(rectangleX1, rectangleY1, rectangleWidth, rectangleHeight);
  }

  @Bean
  public Rectangle getRecipeViewport()
  {
    return determineRectangleDimensions();
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
            RestClient.builder(new HttpHost(elasticsearchHostname, 9200),
                    new HttpHost(elasticsearchHostname, 9201)));
  }

  @Bean
  public ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

}
