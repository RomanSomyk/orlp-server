package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CardRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRatingRepository extends JpaRepository<CardRating, Long> {

    @Query("select avg(c.rating) from CardRating c where c.cardId=:cardId group by c.cardId")
    Double findRatingByCardId(@Param("cardId") long cardId);

    CardRating findCardRatingByAccountEmailAndCardId(String accountEmail, long cardId);
}
