package com.idexx.twitter;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/27/12
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonPropertyOrder(alphabetic=true)
public class Tweet {
    private String id;
    private String created_at;
    private String text;

    public Tweet() {
    }

    public Tweet(String id, String created_at, String text) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedAt() {
        return new DateTime(created_at);
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Tweet)) {
                return false;
        }
        if (o == this) {
            return true;
        }

        Tweet other = (Tweet) o;
        return Objects.equal(id, other.id);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("created_at", created_at)
                .add("text", text)
                .toString();
    }
}
