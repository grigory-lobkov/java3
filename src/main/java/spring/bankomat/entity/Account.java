package spring.bankomat.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

import java.math.BigDecimal;

@CompiledJson(name = "Счет")
public class Account implements IEntity {

    @JsonAttribute(name = "уникальный идентификатор счета")
    private long id;

    @JsonAttribute(name = "владелец счета")
    private String holder;

    @JsonAttribute(name = "сумма на счете")
    private BigDecimal amount;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", holder='" + holder + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
