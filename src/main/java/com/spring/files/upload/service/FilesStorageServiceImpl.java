package com.spring.files.upload.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	private final Path root = Paths.get("./src/main/resources/uploads");
	File rootFilePath = root.toFile();

	@Override
	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

			CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()), ',', '"', 0);

			// Read CSV line by line and use the string array as you want
			String[] nextLine;
			List<String[]> recordList = new ArrayList<String[]>();
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {
					// Verifying the read data here
					System.out.println(Arrays.toString(nextLine));
					if (!nextLine[0].trim().isEmpty()) {
						recordList.add(nextLine);
					}
				}
			}
			reader.close();

			File[] files = rootFilePath.listFiles();
			FileWriter output = new FileWriter(files[0].getAbsolutePath());
			try (CSVWriter write = new CSVWriter(output, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
				write.writeAll(recordList);
			}

		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public String getRecordByKey(String key) throws IOException {
		return readCSV(key);
	}

	private String readCSV(String key) throws IOException {
		// Read existing file
		File[] files = rootFilePath.listFiles();

		String[] strArray = null;
		List<String[]> csvBody;
		try (CSVReader reader = new CSVReader(new FileReader(files[0].getAbsolutePath()), ',')) {
			csvBody = reader.readAll();
		} catch (Exception ex) {
			throw new IOException(ex.getMessage());
		}

		// get CSV row column
		for (int i = 1; i < csvBody.size(); i++) {
			if (csvBody.get(i)[0].equals(key)) {
				strArray = csvBody.get(i);
				break;
			}
		}

		if (strArray == null) {
			throw new IOException("No file found");
		}
		return Arrays.toString(strArray);
	}

	@Override
	public List<String[]> deleteRecordByKey(String key) throws IOException {
		File[] files = rootFilePath.listFiles();

		List<String[]> csvBody;
			try (CSVReader reader = new CSVReader(new FileReader(files[0].getAbsolutePath()), ',')) {
				csvBody = reader.readAll();	
			} catch (Exception ex) {
				throw new IOException(ex.getMessage());
			}
			
			// get CSV row column
			for (int i = 1; i < csvBody.size(); i++) {
				if (csvBody.get(i)[0].equals(key)) {
					csvBody.remove(i);
					break;
				} 
			}
			
			FileWriter fWriter = new FileWriter(files[0].getAbsolutePath());
			try (CSVWriter writer = new CSVWriter(fWriter)) {
				writer.writeAll(csvBody);
			} catch (IOException ex) {
				throw new IOException("Corrupted file: " + ex.getMessage());
			}
		return csvBody;

	}

}
