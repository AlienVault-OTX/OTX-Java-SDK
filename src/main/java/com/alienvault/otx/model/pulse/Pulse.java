package com.alienvault.otx.model.pulse;

import com.alienvault.otx.model.indicator.Indicator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A utility object to represent a Pulse
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pulse {
    private String id;
    private String name;
    private String description;
    private String author_name;
    private String adversary;
    private String tlp;
    @JsonDeserialize(using = OtxDateDeserializer.class)
    private Date modified;
    @JsonDeserialize(using = OtxDateDeserializer.class)
    private Date created;
    private List<String> tags;
    private List<String> references;
    private List<String> industries;
    private List<String> targeted_countries;
    private Integer revision;
    private List<Indicator> indicators;
    private boolean isPublic = true;

    /**
     * The internal ID of the pulse
     *
     * @return string representing the pulse id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * The name of the pulse
     *
     * @return the pulse name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The description of the pulse
     *
     * @return the description provided when the pulse was originally created
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The name of the author of the pulse
     *
     * @return the author who originally submitted the pulse
     */
    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    /**
     * The last date this pulse was updated
     *
     * @return last modification date
     */
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * The date this pulse was created
     *
     * @return creation date
     */
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Tags associted with this pulse
     *
     * @return list of tags
     */
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Industries associated with this pulse
     *
     * @return list of industries
     */
    public List<String> getIndustries(){
        return industries;
    }
    public void setIndustries(List<String> industries){
        this.industries = industries;
    }
    /**
     * Adversary
     */
    public String getAdversary(){
        return adversary;
    }
    public void setAdversary(String adversary){
        this.adversary = adversary;
    }
    /**
     * The list of references associated with this pulse
     *
     * @return a list of references
     */
    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }



    /**
     * The revision of this representation of the pulse
     *
     * @return
     */
    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * The indicators associated with this pulse
     *
     * @return List of indicators
     */
    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }


    @Override
    public String toString() {
        return "Pulse{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", indicators=" + Arrays.deepToString((indicators == null ? new String[]{} : indicators.toArray())) +
                '}';
    }
    @JsonSetter("public")
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    @JsonGetter("public")
    public boolean isPublic() {
        return isPublic;
    }

    @JsonGetter("tlp")
    public String getTlp() {
        return tlp;
    }

    @JsonSetter("tlp")
    public void setTlp(String tlp) {
        this.tlp = tlp;
    }
    @JsonGetter("targeted_countries")
    public List<String> getTargetedCountries(){
        return targeted_countries;
    }
    @JsonSetter("targeted_countries")
    public void setTargetedCountries(List<String> targeted_countries){
        this.targeted_countries = targeted_countries;
    }
}
