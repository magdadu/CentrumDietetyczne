package wat.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Result.
 */
@Entity
@Table(name = "result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "resluts")
    private byte[] resluts;

    @Column(name = "resluts_content_type")
    private String reslutsContentType;

    @ManyToOne
    private Client client_result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getResluts() {
        return resluts;
    }

    public Result resluts(byte[] resluts) {
        this.resluts = resluts;
        return this;
    }

    public void setResluts(byte[] resluts) {
        this.resluts = resluts;
    }

    public String getReslutsContentType() {
        return reslutsContentType;
    }

    public Result reslutsContentType(String reslutsContentType) {
        this.reslutsContentType = reslutsContentType;
        return this;
    }

    public void setReslutsContentType(String reslutsContentType) {
        this.reslutsContentType = reslutsContentType;
    }

    public Client getClient_result() {
        return client_result;
    }

    public Result client_result(Client client) {
        this.client_result = client;
        return this;
    }

    public void setClient_result(Client client) {
        this.client_result = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result result = (Result) o;
        if(result.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Result{" +
            "id=" + id +
            ", resluts='" + resluts + "'" +
            ", reslutsContentType='" + reslutsContentType + "'" +
            '}';
    }
}
