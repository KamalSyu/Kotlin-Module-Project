import java.util.Scanner

class NotesApp {
    private val scanner = Scanner(System.`in`)
    private val archives = mutableListOf<Archive>()

    private val archiveMenu = Menu(
        scanner = scanner,
        elementsProvider = { archives },
        displayElement = { archive, _ ->
            "Созданный архив: ${archive.name}"
        },
        onCreate = { createArchive() },
        onSelect = { archive -> showNotes(archive) },
        onEdit = { archive -> editArchive(archive) }, // Добавляем onEdit для архивов
        exitOptionLabel = "Выйти из приложения",
        exitAction = ::exitApp,
        menuTitle = "Меню архивов",
        onDelete = { deleteArchive(it) },
        createOptionLabel = "Создать новый архив"
    )

    fun start() {
        archiveMenu.showAndHandle()
    }

    fun createArchive() {
        println("Введите название нового архива:")
        print("> ")
        val name = scanner.nextLine().trim()
        if (name.isEmpty()) {
            println("Название архива не может быть пустым.")
            return
        }
        archives.add(Archive(name))
        println("Архив \"$name\" создан.")
    }

    fun createNote(archive: Archive) {
        println("Введите текст новой заметки:")
        print("> ")
        val text = scanner.nextLine().trim()
        if (text.isEmpty()) {
            println("Текст заметки не может быть пустым.")
            return
        }
        archive.notes.add(Note(text))
        println("Заметка добавлена в архив \"${archive.name}\".")
    }

    fun showNoteDetails(note: Note) {
        println("\n--- Детали заметки ---")
        println(note.text)
        println("---------------------")
        println("Нажмите Enter для возврата.")
        scanner.nextLine()
    }

    fun showNotes(archive: Archive) {
        val noteMenu = Menu(
            scanner = scanner,
            elementsProvider = { archive.notes },
            displayElement = { note, _ ->
                "Созданная заметка: ${note.text.take(20)}${if (note.text.length > 20) "..." else ""}"
            },
            onCreate = { createNote(archive) },
            onSelect = { note -> showNoteDetails(note) },
            onEdit = { note -> editNote(note) }, // Добавляем onEdit для заметок
            exitOptionLabel = "Вернуться в меню архивов",
            exitAction = {},
            menuTitle = "Меню заметок архива \"${archive.name}\"",
            onDelete = { deleteNote(it, archive) },
            createOptionLabel = "Создать новую заметку"
        )
        noteMenu.showAndHandle()
    }

    fun editNote(note: Note) {
        println("Введите новый текст заметки:")
        print("> ")
        val newText = scanner.nextLine().trim()
        if (newText.isEmpty()) {
            println("Текст заметки не может быть пустым.")
            return
        }
        note.text = newText
        println("Заметка обновлена.")
    }


    fun editArchive(archive: Archive) {
        println("Введите новое название архива:")
        print("> ")
        val newName = scanner.nextLine().trim()
        if (newName.isEmpty()) {
            println("Название архива не может быть пустым.")
            return
        }
        archive.name = newName
        println("Архив переименован в \"$newName\".")
    }

    fun deleteNote(note: Note, archive: Archive) {
        archive.notes.remove(note)
        println("Заметка удалена.")
    }

    fun deleteArchive(archive: Archive) {
        archives.remove(archive)
        println("Архив удалён.")
    }

    fun exitApp() {
        println("Вы выходите из приложения. До свидания!")
    }
}
