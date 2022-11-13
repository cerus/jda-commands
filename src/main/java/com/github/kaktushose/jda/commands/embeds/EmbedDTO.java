package com.github.kaktushose.jda.commands.embeds;

import java.awt.Color;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * This class is a DTO to serialize and deserialize JDA's embed objects to json. Checkout the discord docs to get
 * information about what each field does exactly.
 *
 * <p>The json object can contain {@code {placeholders}} which can then be injected with values at runtime by calling
 * {@link #injectValue(String, Object)} or {@link #injectValues(Map)}. Alternatively you can use
 * {@link #injectFields(Object...)} to inject the fields of objects.
 *
 * @author Kaktushose
 * @version 2.0.0
 * @see <a href="https://discord.com/developers/docs/resources/channel#embed-object">Discord Embed Documentation</a>
 * @see EmbedCache
 * @since 1.1.0
 */
public class EmbedDTO implements Serializable {

    private static final long serialVersionUID = 0L;
    private String title;
    private String description;
    private String url;
    private String color;
    private String timestamp;
    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
    private Field[] fields;

    public EmbedDTO() {
    }

    public EmbedDTO(final String title,
                    final String description,
                    final String url,
                    final String color,
                    final String timestamp,
                    final Footer footer,
                    final Thumbnail thumbnail,
                    final Image image,
                    final Author author,
                    final Field[] fields) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.color = color;
        this.timestamp = timestamp;
        this.footer = footer;
        this.thumbnail = thumbnail;
        this.image = image;
        this.author = author;
        this.fields = fields;
    }

    public EmbedDTO(final EmbedDTO embedDTO) {
        this.title = embedDTO.getTitle();
        this.description = embedDTO.getDescription();
        this.url = embedDTO.getUrl();
        this.color = embedDTO.getColor();
        this.timestamp = embedDTO.getTimestamp();
        this.footer = new Footer(embedDTO.getFooter());
        this.thumbnail = new Thumbnail(embedDTO.getThumbnail());
        this.image = new Image(embedDTO.getImage());
        this.author = new Author(embedDTO.getAuthor());
        if (embedDTO.getFields() != null) {
            this.fields = new Field[embedDTO.getFields().length];
            for (int i = 0; i < this.fields.length; i++) {
                this.fields[i] = new Field(embedDTO.getFields()[i]);
            }
        }
    }

    @Override
    public String toString() {
        return "Embed{" +
                "title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                ", url='" + this.url + '\'' +
                ", color=" + this.color +
                ", timestamp='" + this.timestamp + '\'' +
                ", footer=" + this.toString(this.footer) +
                ", thumbnail=" + this.toString(this.thumbnail) +
                ", image=" + this.toString(this.image) +
                ", author=" + this.toString(this.author) +
                ", fields=" + Arrays.toString(this.fields) +
                '}';
    }

    private String toString(final Object object) {
        return object == null ? null : object.toString();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public Footer getFooter() {
        return this.footer;
    }

    public void setFooter(final Footer footer) {
        this.footer = footer;
    }

    public Thumbnail getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(final Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(final Author author) {
        this.author = author;
    }

    public Field[] getFields() {
        return this.fields;
    }

    public void setFields(final Field[] fields) {
        this.fields = fields;
    }

    /**
     * Transfers this object to a {@code EmbedBuilder}.
     *
     * @return the {@code EmbedBuilder}
     */
    public EmbedBuilder toEmbedBuilder() {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        if (this.title != null) {
            embedBuilder.setTitle(this.title, this.url);
        }
        if (this.description != null) {
            embedBuilder.setDescription(this.description);
        }
        if (this.color != null) {
            embedBuilder.setColor(Color.decode(this.color));
        }
        if (this.timestamp != null) {
            embedBuilder.setTimestamp(ZonedDateTime.parse(this.timestamp));
        }
        if (this.footer != null) {
            embedBuilder.setFooter(this.footer.getText(), this.footer.getIconUrl());
        }
        if (this.thumbnail != null) {
            embedBuilder.setThumbnail(this.thumbnail.getUrl());
        }
        if (this.image != null) {
            embedBuilder.setImage(this.image.getUrl());
        }
        if (this.author != null) {
            embedBuilder.setAuthor(this.author.getName(), this.author.getUrl(), this.author.getIconUrl());
        }
        if (this.fields != null) {
            for (final Field field : this.fields) {
                embedBuilder.addField(field.getName(), field.getValue(), field.isInline());
            }
        }
        return embedBuilder;
    }

    /**
     * Transfers this object to a {@code MessageEmbed}.
     *
     * @return the {@code MessageEmbed}
     */
    public MessageEmbed toMessageEmbed() {
        return this.toEmbedBuilder().build();
    }

    /**
     * Transfers this object to a {@link net.dv8tion.jda.api.entities.Message}.
     *
     * @return the {@link net.dv8tion.jda.api.entities.Message}
     */
    public MessageCreateBuilder toMessage() {
        return new MessageCreateBuilder().setEmbeds(this.toMessageEmbed());
    }

    /**
     * Attempts to inject {@code {placeholders}} with the values of the given object fields. Therefore, the name of the
     * field must match the name of the {@code {placeholder}}.
     *
     * @param objects the objects to get the fields from
     *
     * @return the current instance to use fluent interface
     */
    public EmbedDTO injectFields(final Object... objects) {
        for (final Object object : objects) {
            for (final java.lang.reflect.Field field : object.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    this.injectValue(field.getName(), field.get(object));
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * Attempts to inject {@code {placeholders}} with the given values.
     *
     * @param values a Map with all values to inject. Key: name of the placeholder. Value: the value to inject
     *
     * @return the current instance to use fluent interface
     */
    public EmbedDTO injectValues(final Map<String, Object> values) {
        values.forEach(this::injectValue);
        return this;
    }

    /**
     * Attempts to inject a {@code {placeholder}} with the given value.
     *
     * @param name   the name of the placeholder
     * @param object the value to inject
     *
     * @return the current instance to use fluent interface
     */
    public EmbedDTO injectValue(final String name, final Object object) {
        if (this.title != null) {
            this.title = this.title.replaceAll(this.pattern(name), this.replacement(object));
        }
        if (this.description != null) {
            this.description = this.description.replaceAll(this.pattern(name), this.replacement(object));
        }
        if (this.url != null) {
            this.url = this.url.replaceAll(this.pattern(name), this.replacement(object));
        }
        if (this.color != null) {
            this.color = this.color.replaceAll(this.pattern(name), this.replacement(object));
        }
        if (this.timestamp != null) {
            this.timestamp = this.timestamp.replaceAll(this.pattern(name), this.replacement(object));
        }
        if (this.footer != null) {
            if (this.footer.iconUrl != null) {
                this.footer.iconUrl = this.footer.iconUrl.replaceAll(this.pattern(name), this.replacement(object));
            }
            if (this.footer.text != null) {
                this.footer.text = this.footer.text.replaceAll(this.pattern(name), this.replacement(object));
            }
        }
        if (this.thumbnail != null) {
            if (this.thumbnail.url != null) {
                this.thumbnail.url = this.thumbnail.url.replaceAll(this.pattern(name), this.replacement(object));
            }
        }
        if (this.image != null) {
            if (this.image.url != null) {
                this.image.url = this.image.url.replaceAll(this.pattern(name), this.replacement(object));
            }
        }
        if (this.author != null) {
            if (this.author.iconUrl != null) {
                this.author.iconUrl = this.author.iconUrl.replaceAll(this.pattern(name), this.replacement(object));
            }
            if (this.author.name != null) {
                this.author.name = this.author.name.replaceAll(this.pattern(name), this.replacement(object));
            }
            if (this.author.url != null) {
                this.author.url = this.author.url.replaceAll(this.pattern(name), this.replacement(object));
            }
        }
        if (this.fields != null) {
            for (final Field field : this.fields) {
                if (field.name != null) {
                    field.name = field.name.replaceAll(this.pattern(name), this.replacement(object));
                }
                if (field.value != null) {
                    field.value = field.value.replaceAll(this.pattern(name), this.replacement(object));
                }
            }
        }
        return this;
    }

    private String pattern(final String name) {
        return String.format(Pattern.quote("{%s}"), name);
    }

    private String replacement(final Object object) {
        return Matcher.quoteReplacement(String.valueOf(object));
    }

    /**
     * Attempts to format all fields with the given arguments. Be aware that this might only be useful for some fields
     * such as title, description or fields.
     *
     * @param args Arguments referenced by the format specifiers in the format string. If there are more arguments than
     *             format specifiers, the extra arguments are ignored. The number of arguments is variable and may be
     *             zero.
     *
     * @return the current instance to use fluent interface
     */
    public EmbedDTO injectFormat(final Object... args) {
        if (this.title != null) {
            this.title = String.format(this.title, args);
        }
        if (this.description != null) {
            this.description = String.format(this.description, args);
        }
        if (this.url != null) {
            this.url = String.format(this.url, args);
        }
        if (this.color != null) {
            this.color = String.format(this.color, args);
        }
        if (this.timestamp != null) {
            this.timestamp = String.format(this.timestamp, args);
        }
        if (this.footer != null) {
            if (this.footer.iconUrl != null) {
                this.footer.iconUrl = String.format(this.footer.iconUrl, args);
            }
            if (this.footer.text != null) {
                this.footer.text = String.format(this.footer.text, args);
            }
        }
        if (this.thumbnail != null) {
            if (this.thumbnail.url != null) {
                this.thumbnail.url = String.format(this.thumbnail.url, args);
            }
        }
        if (this.image != null) {
            if (this.image.url != null) {
                this.image.url = String.format(this.image.url, args);
            }
        }
        if (this.author != null) {
            if (this.author.iconUrl != null) {
                this.author.iconUrl = String.format(this.author.iconUrl, args);
            }
            if (this.author.name != null) {
                this.author.name = String.format(this.author.name, args);
            }
            if (this.author.url != null) {
                this.author.url = String.format(this.author.url, args);
            }
        }
        if (this.fields != null) {
            for (final Field field : this.fields) {
                if (field.name != null) {
                    field.name = String.format(field.name, args);
                }
                if (field.value != null) {
                    field.value = String.format(field.value, args);
                }
            }
        }
        return this;
    }

    public static class Footer {

        private String iconUrl;
        private String text;

        public Footer(final String iconUrl, final String text) {
            this.iconUrl = iconUrl;
            this.text = text;
        }

        public Footer(final Footer footer) {
            if (footer != null) {
                this.iconUrl = footer.getIconUrl();
                this.text = footer.getText();
            }
        }

        public String getIconUrl() {
            return this.iconUrl;
        }

        public String getText() {
            return this.text;
        }

        @Override
        public String toString() {
            return "Footer{" +
                    "iconUrl='" + this.iconUrl + '\'' +
                    ", text='" + this.text + '\'' +
                    '}';
        }
    }

    public static class Thumbnail {

        private String url;

        public Thumbnail(final String url) {
            this.url = url;
        }

        public Thumbnail(final Thumbnail thumbnail) {
            if (thumbnail != null) {
                this.url = thumbnail.getUrl();
            }
        }

        public String getUrl() {
            return this.url;
        }

        @Override
        public String toString() {
            return "Thumbnail{" +
                    "url='" + this.url + '\'' +
                    '}';
        }
    }

    public static class Image {

        private String url;

        public Image(final String url) {
            this.url = url;
        }

        public Image(final Image image) {
            if (image != null) {
                this.url = image.getUrl();
            }
        }

        public String getUrl() {
            return this.url;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "url='" + this.url + '\'' +
                    '}';
        }
    }

    public static class Author {

        private String name;
        private String url;
        private String iconUrl;

        public Author(final String name, final String url, final String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }

        public Author(final Author author) {
            if (author != null) {
                this.name = author.getName();
                this.url = author.getUrl();
                this.iconUrl = author.getIconUrl();
            }
        }

        public String getName() {
            return this.name;
        }

        public String getUrl() {
            return this.url;
        }

        public String getIconUrl() {
            return this.iconUrl;
        }

        @Override
        public String toString() {
            return "Author{" +
                    "name='" + this.name + '\'' +
                    ", url='" + this.url + '\'' +
                    ", iconUrl='" + this.iconUrl + '\'' +
                    '}';
        }
    }

    public static class Field {

        private String name;
        private String value;
        private boolean inline;

        public Field(final String name, final String value, final boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        public Field(final Field field) {
            if (field != null) {
                this.name = field.getName();
                this.value = field.getValue();
                this.inline = field.isInline();
            }
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public boolean isInline() {
            return this.inline;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "name='" + this.name + '\'' +
                    ", value='" + this.value + '\'' +
                    ", inline=" + this.inline +
                    '}';
        }
    }
}
