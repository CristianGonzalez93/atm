package com.atm.repository.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Bill {
  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NonNull
  private Double billValue;
  @NonNull
  private Integer amount;
}
