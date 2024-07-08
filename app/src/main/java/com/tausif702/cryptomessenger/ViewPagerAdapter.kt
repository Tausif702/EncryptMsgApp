import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tausif702.cryptomessenger.frgments.DecryptFragment
import com.tausif702.cryptomessenger.frgments.EncryptFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EncryptFragment()
            1 -> DecryptFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getCount(): Int {
        return 2 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Encrypt"
            1 -> "Decrypt"
            else -> null
        }
    }
}
