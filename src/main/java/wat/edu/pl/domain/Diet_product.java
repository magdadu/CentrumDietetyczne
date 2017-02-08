package wat.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Diet_product.
 */
@Entity
@Table(name = "diet_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Diet_product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 1)
    @Max(value = 3)
    @Column(name = "is_recommended")
    private Integer isRecommended;

    @ManyToOne
    private Diet diet;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsRecommended() {
        return isRecommended;
    }

    public Diet_product isRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
        return this;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public Diet getDiet() {
        return diet;
    }

    public Diet_product diet(Diet diet) {
        this.diet = diet;
        return this;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diet_product diet_product = (Diet_product) o;
        if(diet_product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diet_product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Diet_product{" +
            "id=" + id +
            ", isRecommended='" + isRecommended + "'" +
            '}';
    }
}
