package pl.mazurek.springboot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.TransactionDtDto;
import pl.mazurek.springboot.entity.Transactions;

import java.util.List;


@Repository
public interface TransactionRepo extends JpaRepository<Transactions, Long> {

    Page<Transactions> findByCategoryCode(Categories categories, Pageable pageable);

    @Query(value = "select new pl.mazurek.springboot.entity.TransactionDtDto" +
            "(tr.categoryCode ,count(tr), sum(tr.amount) , min(tr.amount), max(tr.amount), avg(tr.amount)) " +
            "FROM Transactions tr " +
            "WHERE tr.date between :dateFrom and :dateTo " +
            "group by tr.categoryCode")
    List<TransactionDtDto> groupBy(@Param("dateFrom") Long dateFrom, @Param("dateTo") Long dateTo);


    @Query(value = "select new pl.mazurek.springboot.entity.TransactionDtDto" +
            "(tr.categoryCode ,count(tr), sum(tr.amount) , min(tr.amount), max(tr.amount), avg(tr.amount)) " +
            "FROM Transactions tr " +
            "WHERE tr.date between :dateFrom and :dateTo AND tr.categoryCode.categoryCodeId = :categoryCodeId " +
            "group by tr.categoryCode")
    List<TransactionDtDto> groupByCategory(@Param("dateFrom") Long dateFrom,
                                           @Param("dateTo") Long dateTo,
                                           @Param("categoryCodeId") Long categoryCodeId);

}
