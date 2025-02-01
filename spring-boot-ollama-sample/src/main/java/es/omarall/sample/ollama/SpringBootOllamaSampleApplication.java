package es.omarall.sample.ollama;

import es.omarall.ollama.OllamaService;
import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class SpringBootOllamaSampleApplication {

    @Value("${application.model-name}")
    private String MODEL_NAME;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOllamaSampleApplication.class, args);
    }


    @Bean
    SimpleStringStreamResponseProcessor streamResponseProcessor() {
        return new SimpleStringStreamResponseProcessor();
    }

    @Bean
    ApplicationRunner runner(OllamaService ollamaService, SimpleStringStreamResponseProcessor streamResponseProcessor) {
        return args -> {

            // Embedding request（ollamaService.embed方法，是Ollama框架中的一个功能，用于将文本转换为数值向量，主要用于文本相似度计算、信息检索和聚类分析。OllamaService.embed通过将文本转换为向量，使得计算机能够理解和处理文本数据）
//            EmbeddingResponse embeddingResponse = ollamaService.embed(EmbeddingRequest.builder()
//                    .model(MODEL_NAME)
//                    .prompt("Dare to embed this text?")
//                    .build());
//            log.info("1、******* Ollama Embedding response: {}", embeddingResponse.getEmbedding());

            log.info("******* (wait for it)");

            System.out.println("###################################问答请求输出结果###################################");
            // Completion request
            Arrays.asList("中国的首都是哪里?",
                            "把这句话翻译成中文: 'I love cookies!'")
                    .forEach(prompt -> {
                        CompletionResponse response = ollamaService.completion(CompletionRequest.builder()
                                .model(MODEL_NAME).prompt(prompt).build());
                        log.info("2、******* Ollama Completion response: {}", response.getResponse());
                    });

            System.out.println("###################################以流的形式输出结果###################################");
            // Streaming completion
            ollamaService.streamingCompletion(CompletionRequest.builder()
                    .model(MODEL_NAME)
                    .prompt("使用中文介绍一下天安门?")
                    .build(), streamResponseProcessor);

        };
    }

}
