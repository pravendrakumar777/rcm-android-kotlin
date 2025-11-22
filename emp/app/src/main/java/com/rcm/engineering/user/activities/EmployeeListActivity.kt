import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rcm.engineering.user.views.EmployeeViewModel

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var vm: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]

        vm.fetchAllEmployees()

        vm.users.observe(this) { list ->
            // Update RecyclerView
        }

        vm.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}