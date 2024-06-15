package com.amazigh.booknetwork.specification;

import com.amazigh.booknetwork.domain.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

  public static Specification<Book> specification(Integer ownerId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
  }
}
