package br.ce.wcaquino.taskbackend.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {

	private Task todo;

	@Mock
	private TaskRepo taskRepo;

	@InjectMocks
	private TaskController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	public TaskControllerTest() {
		controller = new TaskController();
		todo = new Task();
	}

	@Test
	public void naoDeveSalvarTarefaSemDescricao() {
		todo.setDueDate(LocalDate.now());
		try {
			controller.save(todo);
			fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			assertEquals("Fill the task description", e.getMessage());
		}
	}

	@Test
	public void naoDeveSalvarTarefaSemData() {
		todo.setTask("Descricao");
		TaskController controller = new TaskController();
		try {
			controller.save(todo);
			fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			assertEquals("Fill the due date", e.getMessage());
		}
	}

	@Test
	public void naoDeveSalvarTarefaComDataPassada() {
		todo.setTask("Descricao");
		todo.setDueDate(LocalDate.of(2010, 01, 01));
		try {
			controller.save(todo);
			fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			assertEquals("Due date must not be in past", e.getMessage());
		}
	}

	@Test
	public void naoDeveSalvarTarefaComSucesso() throws ValidationException {
		todo.setTask("Descricao");
		todo.setDueDate(LocalDate.now());
		controller.save(todo);
		Mockito.verify(taskRepo).save(todo);
	}
}
