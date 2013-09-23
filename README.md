# TripletExtraction


Ant targets:

clean - removes build, target and downloads (downloads should be autocleaned anyway)

getDependencies - gets stanford corenlp and log4j

jar - builds the jar
	-- need to provide:
	
		-Dmain_path=path_to_main_class   (the only main currently existing is at com.nextcentury.TripletExtraction.TestDriver)
		
		-Djar_name=base_name_of_resulting package (dont include the .jar.... defaults to package resulting in package.jar)
