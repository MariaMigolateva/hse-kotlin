import java.time.LocalDateTime
import kotlin.system.exitProcess

fun main() {
    val expenseTracker = ExpenseTracker()
    expenseTracker.run()
}

class ExpenseTracker {
    private var balance = 0.0
    private val transactionHistory = mutableListOf<Transaction>()
    private val categories = mutableSetOf("Food", "Utilities", "Entertainment")

    fun run() {
        while (true) {
            displayMenu()
            val command = readLine()!!
            handleInput(command)
        }
    }

    private fun displayMenu() {
        println("""
            |1. Display Balance
            |2. Add Expense
            |3. Add Income
            |4. Undo Last Transaction
            |5. Display Transaction History
            |6. Add Category
            |7. Display Transactions by Category
            |8. Exit
            |Enter command: 
        """.trimMargin())
    }

    private fun handleInput(command: String) {
        when (command) {
            "1" -> displayBalance()
            "2" -> addTransaction(readTransactionAmount(), "Expense", readCategory())
            "3" -> addTransaction(readTransactionAmount(), "Income")
            "4" -> undoLastTransaction()
            "5" -> displayHistory()
            "6" -> addCategory()
            "7" -> displayByCategory()
            "8" -> exitProcess(0)
            else -> println("Invalid command, please try again.")
        }
    }

    private fun readTransactionAmount(): Double {
        println("Enter amount: ")
        return readLine()?.toDoubleOrNull()?.takeIf { it > 0 }
            ?: run {
                println("Invalid input. Please enter a valid amount.")
                readTransactionAmount()
            }
    }

    private fun addTransaction(amount: Double, type: String, category: String? = null) {
        if (type == "Expense") balance -= amount else if (type == "Income") balance += amount
        transactionHistory.add(Transaction(amount, type, category, LocalDateTime.now()))
        println("$type of $$amount added.")
    }

    private fun displayBalance() {
        println("Current balance: $$balance")
    }

    private fun undoLastTransaction() {
        if (transactionHistory.isNotEmpty()) {
            val lastTransaction = transactionHistory.removeAt(transactionHistory.size - 1)
            if (lastTransaction.type == "Expense") balance += lastTransaction.amount
            else if (lastTransaction.type == "Income") balance -= lastTransaction.amount
            println("Last transaction undone.")
        } else {
            println("No transactions to undo.")
        }
    }

    private fun displayHistory() {
        if (transactionHistory.isEmpty()) {
            println("Transaction history is empty.")
        } else {
            transactionHistory.forEach { println(it) }
        }
    }

    private fun addCategory() {
        println("Enter new category name:")
        val newCategory = readLine()!!
        if (categories.add(newCategory)) {
            println("Category '$newCategory' added.")
        } else {
            println("Category '$newCategory' already exists.")
        }
    }

    private fun displayByCategory() {
        println("Enter category to display:")
        val category = readLine()!!
        if (category !in categories) {
            println("Category '$category' does not exist.")
            return
        }
        val filteredTransactions = transactionHistory.filter { it.category == category }
        if (filteredTransactions.isEmpty()) {
            println("No transactions found for category '$category'.")
        } else {
            filteredTransactions.forEach { println(it) }
        }
    }

    private fun readCategory(): String {
        println("Enter category (Optional, press Enter to skip):")
        val category = readLine()!!
        return if (category.isNotEmpty() && category in categories) category else "General"
    }
}

data class Transaction(val amount: Double, val type: String, val category: String?, val dateTime: LocalDateTime)