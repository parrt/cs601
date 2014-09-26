import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/** C O O K I E  S T U F F */
public class CookieMonster {
	/** Find a cookie by name; return first found */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] allCookies;

		if ( name==null ) {
			throw new IllegalArgumentException("cookie name is null");
		}

		allCookies = request.getCookies();
		if (allCookies != null) {
			for (int i=0; i < allCookies.length; i++) {
				Cookie candidate = allCookies[i];
				if (name.equals(candidate.getName()) ) {
					return candidate;
				}
			}
		}

		return null;
	}

	/** Find a cookie by name; return first found */
	public static String getCookieValue(HttpServletRequest request,
										String name)
	{
		Cookie c = getCookie(request, name);
		if ( c!=null ) {
			return c.getValue();
		}
		return null;
	}

	/** Set a cookie by name */
	public static void setCookieValue(HttpServletResponse response,
									  String name,
									  String value)
	{
		Cookie c = new Cookie(name,value);
		c.setMaxAge( 3 * 30 * 24 * 60 * 60 ); // 3 months
		c.setPath( "/" );
		response.addCookie( c );
	}

	private void killCookie(HttpServletResponse response, String name) {
		Cookie c = new Cookie(name,"false");
		c.setMaxAge( 0 ); // An age of 0 is defined to mean "delete cookie"
		c.setPath( "/" ); // for all subdirs
		response.addCookie( c );
	}
}
