package top.imwonder.mcauth.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebTexture {

    private String id;
    private String name;
    private List<WebTextureEntity> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WebTextureEntity getProperties() {
        return properties.get(0);
    }

    public void setProperties(List<WebTextureEntity> properties) {
        this.properties = properties;
    }

    public static class WebTextureEntity {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
