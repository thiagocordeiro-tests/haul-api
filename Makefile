up:
	docker-compose up -d --build
	make logs

logs:
	docker-compose logs -f

down:
	docker-compose down

test:
	./gradlew test clean
