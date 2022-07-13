package osm.surveyor.task.user.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import osm.surveyor.task.user.model.SiteUser;
import osm.surveyor.task.util.Role;

import osm.surveyor.task.user.repository.SiteUserRepository;

@SpringBootTest
@Transactional
class UserDetailsServiceImplTest {
	
	@Autowired
	SiteUserRepository repository;
	
	@Autowired
	UserDetailsServiceImpl service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    @DisplayName("ユーザ名が存在する時、ユーザ詳細を取得することを期待します")
	void test() {
        // Arrange(準備する)
        var user = new SiteUser();
        user.setUsername("Harada");
        user.setPassword("password");
        user.setEmail("harada@example.com");
        user.setRole(Role.USER.name());
        repository.save(user);

        // Act(実行する)
        var actual = service.loadUserByUsername("Harada");

        // Assert(検証する)
        assertEquals(user.getUsername(), actual.getUsername());
	}

    @Test
    @DisplayName("ユーザ名が存在しない時、例外をスローします")
    void whenUsernameDoesNotExist_throwException() {
        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("Takeda"));
    }
}
