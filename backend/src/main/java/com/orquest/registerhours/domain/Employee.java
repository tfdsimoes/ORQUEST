package com.orquest.registerhours.domain;

import com.orquest.registerhours.domain.alert.Alert;
import com.orquest.registerhours.domain.register.Register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "employee" )
public class Employee {

    @Id
    @Column( name = "id" )
    @GeneratedValue( generator = "system-uuid" )
    @GenericGenerator( name = "system-uuid", strategy = "uuid" )
    private String id;

    @Column( name = "number", nullable = false )
    private String number;

    @Builder.Default
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn( name = "employee_id" )
    private List<Alert> alerts = new ArrayList<>();

    @Builder.Default
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn( name = "employee_id" )
    private List<Register> registers = new ArrayList<>();
}
