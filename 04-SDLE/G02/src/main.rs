extern crate petgraph;
extern crate rand;
extern crate clap;
extern crate criterion_plot as plot;

use petgraph::{Graph, Undirected};
use petgraph::graph::NodeIndex;
use petgraph::algo::{is_cyclic_undirected, connected_components};
use petgraph::dot::{Dot, Config};

use rand::Rng;
use std::ops::{Index, IndexMut};
use clap::{App, Arg, SubCommand};
use plot::prelude::*;
use std::str::FromStr;

const DEFAULT_NODES: usize = 100;

fn select_by_degree(graph: &Graph<usize, (), Undirected>, mut selected: i32) -> NodeIndex {
    for node_idx in graph.node_indices() {
        let weight = graph.index(node_idx);
        selected -= *weight as i32;

        if selected <= 0 {
            return node_idx;
        }
    }

    unreachable!();
}

fn build_preferential_graph(nodes: usize) -> Graph<usize, (), Undirected> {
    let mut graph = Graph::<_, (), Undirected>::new_undirected();

    for _ in 0..nodes {
        graph.add_node(1);
    }

    let indices: Vec<NodeIndex> = graph.node_indices().collect();
    let mut rnd = rand::thread_rng();
    let mut max = nodes;

    while connected_components(&graph) > 1 {
        let selected: i32 = rnd.gen_range(0, max as i32);
        let a = rnd.choose(&indices).unwrap();
        let b = select_by_degree(&graph, selected);

        let edge = graph.update_edge(*a, b, ());

        if is_cyclic_undirected(&graph) {
            graph.remove_edge(edge);
        } else {
            *graph.index_mut(*a) += 1;
            *graph.index_mut(b) += 1;
            max += 2;
        }
    }

    return graph;
}

fn nodes_with_degree(graph: &Graph<usize, (), Undirected>, degree: usize) -> usize {
    let nodes = graph.raw_nodes();
    nodes.iter().filter(|n| n.weight == degree).count()
}

fn plot_power_wall(graph: &Graph<usize, (), Undirected>) {
    let max_degree = graph.raw_nodes().iter().map(|n| n.weight).max().unwrap();
    let ref xs: Vec<usize> = (2..max_degree+1).collect();

    Figure::new()
		.configure(Axis::BottomX, |a| {
			a.set(Label("Degree"))
			 .configure(Grid::Major, |g| g.show())
		})
		.configure(Axis::LeftY, |a| {
			a.set(Label("Number of nodes"))
			 .configure(Grid::Major, |g| g.show())
		})
		.plot(LinesPoints {
			x: xs,
            y: xs.iter().map(|x| nodes_with_degree(&graph, *x))
		}, |lp| {
			lp.set(PointType::FilledCircle)
			  .set(PointSize(0.6))
		})
		.draw()
		.ok()
		.and_then(|gnuplot| {
			gnuplot.wait_with_output().ok().and_then(|p| String::from_utf8(p.stderr).ok())
		});
}

fn main() {
    let matches = App::new("preferential-graph")
                          .about("Allows some sampling with preferential graphs")
                          .arg(Arg::with_name("N")
                                .global(true)
                                .help("Number of nodes"))
                          .subcommand(SubCommand::with_name("generate"))
                          .subcommand(SubCommand::with_name("plot"))
                          .get_matches();

    let nodes = matches.value_of("N")
        .and_then(|s| usize::from_str(s).ok())
        .unwrap_or(DEFAULT_NODES);

    let graph = build_preferential_graph(nodes);

    if matches.is_present("plot") {
        plot_power_wall(&graph);
    } else {
        println!("{:?}", Dot::with_config(&graph, &[Config::EdgeNoLabel]));
    }
}
