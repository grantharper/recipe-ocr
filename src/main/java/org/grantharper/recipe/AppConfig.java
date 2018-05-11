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
import org.grantharper.recipe.ocr.RectangleCoordinateCsvParser;
import org.grantharper.recipe.ocr.RectangleProvider;
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

  @Value("${elasticsearch.hostname}")
  private String elasticsearchHostname;

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
