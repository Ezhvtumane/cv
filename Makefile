build-jar:
	./mvnw clean package

build-image:
	docker build . -t georgyorlov.com/cv:latest

build-all:
	./mvnw clean package
	docker build . -t georgyorlov.com/cv:latest
