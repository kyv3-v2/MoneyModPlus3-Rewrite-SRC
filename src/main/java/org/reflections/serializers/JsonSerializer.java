package org.reflections.serializers;

import org.reflections.*;
import org.reflections.util.*;
import java.nio.charset.*;
import com.google.common.io.*;
import java.io.*;
import java.lang.reflect.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.gson.*;

public class JsonSerializer implements Serializer
{
    private Gson gson;
    
    @Override
    public Reflections read(final InputStream inputStream) {
        return (Reflections)this.getGson().fromJson((Reader)new InputStreamReader(inputStream), (Class)Reflections.class);
    }
    
    @Override
    public File save(final Reflections reflections, final String filename) {
        try {
            final File file = Utils.prepareFile(filename);
            Files.write((CharSequence)this.toString(reflections), file, Charset.defaultCharset());
            return file;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        return this.getGson().toJson((Object)reflections);
    }
    
    private Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder().registerTypeAdapter((Type)Multimap.class, (Object)new com.google.gson.JsonSerializer<Multimap>() {
                public JsonElement serialize(final Multimap multimap, final Type type, final JsonSerializationContext jsonSerializationContext) {
                    return jsonSerializationContext.serialize((Object)multimap.asMap());
                }
            }).registerTypeAdapter((Type)Multimap.class, (Object)new JsonDeserializer<Multimap>() {
                public Multimap deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    final SetMultimap<String, String> map = (SetMultimap<String, String>)Multimaps.newSetMultimap((Map)new HashMap(), (Supplier)new Supplier<Set<String>>() {
                        public Set<String> get() {
                            return (Set<String>)Sets.newHashSet();
                        }
                    });
                    for (final Map.Entry<String, JsonElement> entry : ((JsonObject)jsonElement).entrySet()) {
                        for (final JsonElement element : (JsonArray)entry.getValue()) {
                            map.get((Object)entry.getKey()).add(element.getAsString());
                        }
                    }
                    return (Multimap)map;
                }
            }).setPrettyPrinting().create();
        }
        return this.gson;
    }
}
