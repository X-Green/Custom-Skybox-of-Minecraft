package dev.eeasee.custom_skybox.resources;

import com.google.common.collect.ImmutableList;
import dev.eeasee.custom_skybox.utils.DimensionTypeEnum;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SkyProperty {

    private static final Logger LOGGER = LogManager.getLogger();

    public static List<SkyProperty> getSkies(ResourceManager resourceManager) {

        final String[] NAMESPACES = {"eeasee_custom_skybox", "minecraft"};
        final String[][] PATHS_ARRAY = {{"sky/overworld", "sky/the_nether", "sky/the_end"}, {"optifine/sky/world0"}};

        List<SkyProperty> skyProperties = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            String namespace = NAMESPACES[i];
            String[] paths = PATHS_ARRAY[i];
            for (int j = 0; j < paths.length; j++) {
                String path = paths[j];
                int skyIndex = 0;
                while (true) {
                    Identifier skyIdentifier = new Identifier(namespace, path + "/sky" + skyIndex + ".properties");
                    if (resourceManager.containsResource(skyIdentifier)) {
                        try {
                            SkyProperty skyProperty = SkyProperty.of(DimensionTypeEnum.values()[j], skyIdentifier, path, resourceManager);
                            skyProperties.add(skyProperty);
                        } catch (IOException e) {
                            LOGGER.error(e);
                        }
                        skyIndex++;
                    } else {
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(skyProperties);
    }

    private static SkyProperty of(DimensionTypeEnum dimensionType, Identifier resourceLocation, String parentPath, ResourceManager resourceManager) throws IOException {
        InputStream inputStream = resourceManager.getResource(resourceLocation).getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);

        Identifier source = getSource(properties, resourceLocation.getNamespace(), parentPath, resourceManager);

        return new SkyProperty();
    }

    private static Identifier getSource(Properties properties, String namespace, String parentPath, ResourceManager resourceManager) {
        String rawString = properties.getProperty("source");
        String sourceString;
        if (rawString == null) {
            //resourceManager.findResources()
        } else {
            sourceString = (rawString.charAt(0) == '.') ? parentPath.concat(rawString.substring(1)) : rawString;
        }
        return null;
    }


    private SkyProperty() {

    }
}
