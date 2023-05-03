package com.cabify.carpooling.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Journey implements Serializable {

    @Id
    private Long id;
    @Min(value = 1, message = "The journey should have at least one passenger")
    @Max(value = 6, message = "The journey can have up to 6 passengers")
    private int people;
    @ManyToOne
    private Car assignedTo;

    @CreatedDate
    private Date createdDate;

    private boolean completed = false;

}
