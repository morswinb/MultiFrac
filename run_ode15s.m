function [y] = run_ode15s(obj,t_start,t_end,y_0,retol,abstol)
            
            options = odeset('RelTol',retol,'AbsTol',abstol,...
                'Stats','off','BDF','on');
				
			%obj.JPattern()

            [T,Y]=ode15s(@(t,w)obj.dt(t,w),[t_start t_end],y_0,options); 	
            y=Y(end,:);
            
end
       
