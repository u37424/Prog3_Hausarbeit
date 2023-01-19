package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Arrays;

public class Publisher {
    private Publisher[] publishers;
    private int publisherId;
    private String name;
    private int foundation;
    private String country;

    @JsonCreator
    public Publisher(@JsonProperty("publisherId") int publisherId) {
        this.publisherId = publisherId;
    }

    @JsonGetter("publishers")
    public Publisher[] getPublishers() {
        return publishers;
    }

    @JsonSetter("publishers")
    public void setPublishers(Publisher[] publishers) {
        this.publishers = publishers;
    }

    @JsonGetter("publisherId")
    public int getPublisherId() {
        return publisherId;
    }

    @JsonSetter("publisherId")
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("foundation")
    public int getFoundation() {
        return foundation;
    }

    @JsonSetter("foundation")
    public void setFoundation(int foundation) {
        this.foundation = foundation;
    }

    @JsonGetter("country")
    public String getCountry() {
        return country;
    }

    @JsonSetter("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "publishers=" + Arrays.toString(publishers) +
                ", publisherId=" + publisherId +
                ", name='" + name + '\'' +
                ", foundation=" + foundation +
                ", country='" + country + '\'' +
                '}';
    }
}
