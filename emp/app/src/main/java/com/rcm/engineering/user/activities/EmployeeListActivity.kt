import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.MainActivity
import com.rcm.engineering.user.activities.ProfilesActivity
import com.rcm.engineering.user.adapter.EmployeeAdapter
import com.rcm.engineering.user.databinding.ActivityEmployeeListBinding
import com.rcm.engineering.user.views.EmployeeViewModel

class EmployeeListActivity : AppCompatActivity() {
    private lateinit var vm: EmployeeViewModel
    private lateinit var adapter: EmployeeAdapter
    private lateinit var binding: ActivityEmployeeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerEmployees.layoutManager = LinearLayoutManager(this)

        adapter = EmployeeAdapter(
            mutableListOf(),
            on = { employee ->
                val intent = Intent(this, ProfilesActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_EMPLOYEE, employee)
                startActivity(intent)
            },

            onDelete = {
                vm.deleteEmployee(it.id!!)
            }
        )

        binding.recyclerEmployees.adapter = adapter
        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]
        vm.fetchAllEmployees()

        vm.users.observe(this) { list ->
            adapter.setList(list)
        }

        vm.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}