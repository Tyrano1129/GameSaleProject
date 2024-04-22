package kr.game.sale.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
public class QueryDSLConfig {
    @PersistenceContext
    private EntityManager em;
    @Bean
    public JPAQueryFactory jpaqueryFactory(){
        return new JPAQueryFactory(em);
    }
}
