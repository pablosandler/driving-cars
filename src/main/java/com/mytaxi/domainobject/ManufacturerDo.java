package com.mytaxi.domainobject;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "manufacturer",
    uniqueConstraints = @UniqueConstraint(name = "uc_name", columnNames = {"name"})
)
public class ManufacturerDo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated;


    @Column(nullable = false)
    @NotNull(message = "Name can not be null!")
    private String name;

    public ManufacturerDo(String name) {
        this.id=null;
        this.name = name;
        dateCreated = ZonedDateTime.now();
    }

    private ManufacturerDo(){}

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufacturerDo that = (ManufacturerDo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dateCreated, that.dateCreated) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreated, name);
    }
}
