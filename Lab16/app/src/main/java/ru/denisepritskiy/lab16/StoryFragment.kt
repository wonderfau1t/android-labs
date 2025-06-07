package ru.denisepritskiy.lab16

data class StoryFragment(
    val text: String,
    val imageResId: Int, // ID ресурса иллюстрации
    val actions: List<StoryAction> // Список возможных действий
)

data class StoryAction(
    val label: String, // Текст на кнопке ("Далее", "Назад", "Спеть песенку")
    val destination: String // ID фрагмента, куда ведет действие
)