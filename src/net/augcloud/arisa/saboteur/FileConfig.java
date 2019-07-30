package net.augcloud.arisa.saboteur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileConfig extends YamlConfiguration {

	protected final DumperOptions yamlOptions;
	protected final Representer yamlRepresenter;
	protected final Yaml yaml;
	protected static FileConfiguration config;

	public FileConfig() {
		this.yamlOptions = new DumperOptions();
		this.yamlRepresenter = new YamlRepresenter();
		this.yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
	}

	public FileConfig(final File file) {
		this.yamlOptions = new DumperOptions();
		this.yamlRepresenter = new YamlRepresenter();
		this.yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
		FileConfig.config = loadConfiguration(file);
	}

	public static void init(final File file) {
		FileConfig.config = loadConfiguration(file);
	}

	public static YamlConfiguration loadConfiguration(final File file) {
		Validate.notNull(file, "File cannot be null");
		final YamlConfiguration config1 = new FileConfig();
		try {
			config1.load(file);
		} catch (FileNotFoundException ex3) {
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		} catch (InvalidConfigurationException ex2) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex2);
		}
		return config1;
	}

	@Override
	public String saveToString() {
		this.yamlOptions.setIndent(this.options().indent());
		this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		final String header = this.buildHeader();
		String dump = this.yaml.dump(this.getValues(false));
		if (dump.equals("{}\n")) {
			dump = "";
		}
		return header + dump;
	}

	@Override
	public void load(final File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		Validate.notNull(file, "File cannot be null");
		final FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, Charsets.UTF_8);
		this.load(isr);
		isr.close();
	}

	@Override
	public void save(final File file) throws IOException {
		Validate.notNull(file, "File cannot be null");
		Files.createParentDirs(file);
		final String data = this.saveToString();
		final Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
		try {
			writer.write(data);
		} finally {
			writer.close();
		}
	}

}
