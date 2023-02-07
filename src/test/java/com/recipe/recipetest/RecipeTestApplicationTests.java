package com.recipe.recipetest;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@DataMongoTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RecipeTestApplicationTests {

	@Ignore
	@Test
	void contextLoads() {
	}

}
