package com.emailplatform.userservice.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@Audited
@Entity
@Table(name = "account")
@NoArgsConstructor
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountReference;

    @Column(nullable = false)
    private BigDecimal usedQuotaGB;

    private LocalDateTime subscriptionEndDate;

    private LocalDateTime subscriptionStartDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "quota_id", referencedColumnName = "id")
    private QuotaEntity latestSubscription;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public AccountEntity(UserEntity user, String accountReference) {
        this.user = user;
        this.accountReference = accountReference;
        this.usedQuotaGB = BigDecimal.ZERO;
    }
}
