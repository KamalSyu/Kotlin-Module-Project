import java.util.Scanner

class Menu<T>(
    private val scanner: Scanner,
    private val elementsProvider: () -> List<T>,
    private val displayElement: (T, Int) -> String,
    private val onCreate: () -> Unit,
    private val onSelect: (T) -> Unit,
    private val onEdit: (T) -> Unit,
    private val exitOptionLabel: String,
    private val exitAction: () -> Unit,
    private val menuTitle: String,
    private val createOptionLabel: String,
    private val onDelete: (T) -> Unit
) {

    private val ANSI_RESET = "\u001B[0m"
    private val ANSI_RED = "\u001B[31m"
    private val ANSI_GREEN = "\u001B[32m"
    private val ANSI_YELLOW = "\u001B[33m"
    private val ANSI_CYAN = "\u001B[36m"
    private val ANSI_BOLD = "\u001B[1m"

    fun showAndHandle() {
        while (true) {
            println()
            println("${ANSI_BOLD}${ANSI_CYAN}=== $menuTitle ===${ANSI_RESET}")

            val items = elementsProvider()

            if (items.isEmpty()) {
                println("${ANSI_YELLOW}Список пуст.${ANSI_RESET}")
            }

            var optionIndex = 0

            // Пункты меню
            println("$optionIndex. $createOptionLabel")
            optionIndex++

            if (items.isNotEmpty()) {
                items.forEachIndexed { index, element ->
                    println("$optionIndex. ${displayElement(element, index)}")
                    optionIndex++
                }
            }

            println("$optionIndex. $exitOptionLabel")

            println("${ANSI_GREEN}Введите номер пункта и нажмите Enter.${ANSI_RESET}")
            print("> ")
            val input = scanner.nextLine()
            val choice = input.toIntOrNull()

            if (choice == null) {
                println("${ANSI_RED}Ошибка: введите корректный номер пункта.${ANSI_RESET}")
                continue
            }

            when {
                choice == 0 -> {
                    onCreate()
                }

                choice in 1 until optionIndex && items.isNotEmpty() -> {
                    val selectedItem = items[choice - 1]
                    println("Выберите, пожалуйста, действие: 1 - Просмотреть, 2 - Редактировать, 3 - Удалить")
                    print("> ")
                    val action = scanner.nextLine().toIntOrNull()
                    when (action) {
                        1 -> onSelect(selectedItem)
                        2 -> onEdit(selectedItem)
                        3 -> onDelete(selectedItem)
                        else -> {
                            println("${ANSI_RED}Ошибка: такого пункта нет. Попробуйте ещё раз.${ANSI_RESET}")
                        }
                    }
                }

                choice == optionIndex -> {
                    exitAction()
                    return
                }

                else -> {
                    println("${ANSI_RED}Ошибка: такого пункта нет. Попробуйте ещё раз.${ANSI_RESET}")
                }
            }
        }
    }
}