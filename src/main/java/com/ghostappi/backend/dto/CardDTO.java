package com.ghostappi.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CardDTO {
    private Integer idCard;

    @Positive(message = "Card number must be a positive integer")
    @Min(value = 1, message = "The card number min value is 1")
    @Max(value = 999999999, message = "The card number max value is 999999999")
    @NotNull(message = "The card number must contain at least one digit")
    private Integer number;

    @NotBlank(message = "Card type cannot be blank")
    @Size(min = 1, max = 30, message = "The type must be almost 1 character and 30 characters at most")
    private String type;

    @NotNull(message = "Expiration date cannot be null")
    @Future(message = "Expiration date must be in the future")
    private Date expirationDate;

    @NotNull(message = "The card number must contain at least one digit")
    @Positive(message = "CVV must be a positive integer")
    @Min(value = 0, message = "The cvv number min value is 0")
    @Max(value = 999, message = "The cvv number max value is 999")
    private Integer cvv;
    @NotNull(message = "The expiration can't be null")
    private Boolean expired;

    @NotNull(message = "Wallet ID cannot be null")
    private Integer walletId;

    // Getters and setters
    public Integer getIdCard() {
        return idCard;
    }

    public void setIdCard(Integer idCard) {
        this.idCard = idCard;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }
}