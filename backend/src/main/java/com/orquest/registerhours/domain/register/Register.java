package com.orquest.registerhours.domain.register;

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
import java.time.ZonedDateTime;

/**
 * Register entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "register" )
public class Register {

    @Id
    @Column( name = "id" )
    @GeneratedValue( generator = "system-uuid" )
    @GenericGenerator( name = "system-uuid", strategy = "uuid" )
    private String id;

    @Column( name = "business_id", nullable = false )
    private String businessId;

    @Column( name = "date", nullable = false )
    private ZonedDateTime date;

    @Column( name = "record_type", nullable = false)
    @Enumerated( value = EnumType.STRING )
    private RegisterRecordType recordType;

    @Column( name = "service_id", nullable = false )
    private String serviceId;

    @Column( name = "type", nullable = false)
    @Enumerated( value = EnumType.STRING )
    private RegisterType type;

    @ManyToOne
    @JoinColumn( name = "employee_id" )
    private Employee employee;
}
