package com.orquest.registerhours.domain.alert;

import com.orquest.registerhours.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Alert entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "alert" )
public class Alert {

    @Id
    @Column( name = "id" )
    @GeneratedValue( generator = "system-uuid" )
    @GenericGenerator( name = "system-uuid", strategy = "uuid" )
    private String id;

    @Column( name = "alert_type", nullable = false )
    @Enumerated( value = EnumType.STRING )
    private AlertType type;

    @Column( name = "date", nullable = false )
    private LocalDate date;

    @Column ( name = "notes" )
    private String notes;

    @ManyToOne
    @JoinColumn( name = "employee_id" )
    private Employee employee;
}
